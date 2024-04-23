package garage.reservation.fastandfurious.service;

import garage.reservation.fastandfurious.domain.Appointment;
import garage.reservation.fastandfurious.domain.GarageOperatingDay;
import garage.reservation.fastandfurious.domain.GarageOperationType;
import garage.reservation.fastandfurious.domain.Mechanic;
import garage.reservation.fastandfurious.domain.OperatingHours;
import garage.reservation.fastandfurious.domain.Slot;
import garage.reservation.fastandfurious.repository.AppointmentRepository;
import garage.reservation.fastandfurious.repository.MechanicRepository;
import garage.reservation.fastandfurious.service.exception.CannotFindSlotsForPastDatesException;
import garage.reservation.fastandfurious.service.exception.MechanicNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@Service
public class ReservationService {

    private final MechanicService mechanicService;
    private final AppointmentRepository appointmentRepository;
    private final OperatingHoursService operatingHoursService;
    private final AppointmentValidator appointmentValidator;

    public ReservationService(MechanicService mechanicService, AppointmentRepository appointmentRepository, OperatingHoursService operatingHoursService, AppointmentValidator appointmentValidator) {
        this.mechanicService = mechanicService;
        this.appointmentRepository = appointmentRepository;
        this.operatingHoursService = operatingHoursService;
        this.appointmentValidator = appointmentValidator;
    }

    private static Predicate<Appointment> appointmentOverlaps(LocalDateTime proposedStart, LocalDateTime proposedEnd) {

        return appointment -> proposedStart.isBefore(appointment.getEndTime()) && proposedEnd.isAfter(appointment.getStartTime());
    }

    private static List<Slot> calculateTimeSlots(GarageOperationType garageOperationType, GarageOperatingDay operatingDay) {

        LocalTime currentTime = operatingDay.getOpeningTime();
        long durationOfJobTypeInMinutes = (long) (garageOperationType.getDurationHours() * 60);

        List<Slot> availableSlots = new ArrayList<>();

        while (currentTime.plusMinutes(durationOfJobTypeInMinutes).isBefore(operatingDay.getClosingTime()) || currentTime.plusMinutes(durationOfJobTypeInMinutes).equals(operatingDay.getClosingTime())) {

            LocalDateTime proposedStart = LocalDateTime.of(operatingDay.getDate(), currentTime);
            LocalDateTime proposedEnd = proposedStart.plusMinutes(durationOfJobTypeInMinutes);

            // Check for overlapping appointments
            boolean isOverlapping = operatingDay.getAppointments().stream().anyMatch(appointmentOverlaps(proposedStart, proposedEnd));

            if (!isOverlapping) {
                availableSlots.add(Slot.builder().startTime(proposedStart).endTime(proposedEnd).build());
            }

            // Increment the slot checker
            currentTime = currentTime.plusMinutes(durationOfJobTypeInMinutes);
        }
        return availableSlots;
    }

    public List<Slot> findAvailableSlots(GarageOperationType garageOperationType, Long mechanicId, LocalDate date) {

        Mechanic mechanic = mechanicService.getById(mechanicId);

        if (mechanic.isOff(date)) {
            // If Mechanic is off that day, then there is no available slots. No need to do slot calculations
            return Collections.emptyList();
        }

        GarageOperatingDay operatingDay = getGarageOperatingDayDetails(date, mechanicId);

        // Garage is not operating that day [SUNDAY, Holiday]
        if (operatingDay.isOff()) {
            return Collections.emptyList();
        }

        // Calculate time slots considering job duration
        return calculateTimeSlots(garageOperationType, operatingDay);
    }

    @Transactional
    public Appointment createAppointment(Long mechanicId, GarageOperationType garageOperationType, LocalDateTime startTime) {

        // Fetch the mechanic from the database
        Mechanic mechanic = mechanicService.getById(mechanicId);

        // Calculate the end time based on job type duration
        LocalDateTime endTime = startTime.plusHours((long) garageOperationType.getDurationHours());

        appointmentValidator.validate(mechanic, startTime, endTime);

        // If no overlaps, create and save the new appointment
        Appointment newAppointment = new Appointment();
        newAppointment.setMechanic(mechanic);
        newAppointment.setGarageOperationType(garageOperationType);
        newAppointment.setStartTime(startTime);
        newAppointment.setEndTime(endTime);
        newAppointment.setDuration(garageOperationType.getDurationHours());

        return appointmentRepository.save(newAppointment);
    }

    private GarageOperatingDay getGarageOperatingDayDetails(LocalDate date, Long mechanicId) {

        OperatingHours operationHours = operatingHoursService.findOperatingHoursForDate(date);
        LocalTime openingTime = operationHours.getOpenTime();
        LocalTime closingTime = operationHours.getCloseTime();

        if (operationHours.isOffDay()) {
            return GarageOperatingDay.builder()
                    .openingTime(openingTime)
                    .closingTime(closingTime)
                    .date(date)
                    .build();
        }

        // Fetch existing appointments
        List<Appointment> existingAppointments = appointmentRepository.findByMechanicBetweenGivenSlot(
                mechanicId,
                LocalDateTime.of(date, openingTime),
                LocalDateTime.of(date, closingTime));

        return GarageOperatingDay.builder()
                .appointments(existingAppointments)
                .openingTime(openingTime)
                .closingTime(closingTime)
                .date(date)
                .build();
    }
}
