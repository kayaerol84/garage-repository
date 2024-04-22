package garage.reservation.fastandfurious.service;

import garage.reservation.fastandfurious.domain.Appointment;
import garage.reservation.fastandfurious.domain.Mechanic;
import garage.reservation.fastandfurious.repository.AppointmentRepository;
import garage.reservation.fastandfurious.service.exception.InvalidAppointmentDateTimeException;
import garage.reservation.fastandfurious.service.exception.MechanicNotAvailableException;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentValidator {

    private final AppointmentRepository appointmentRepository;
    private final TimeService timeService;

    public AppointmentValidator(AppointmentRepository appointmentRepository, TimeService timeService) {
        this.appointmentRepository = appointmentRepository;
        this.timeService = timeService;
    }

    public void validate(Mechanic mechanic, LocalDateTime startTime, LocalDateTime endTime){
        validateMechanic(mechanic, startTime.toLocalDate());
        validateAppointmentDate(startTime, endTime);
        validateOverlappingAppointments(mechanic, startTime, endTime);
    }

    private void validateMechanic(Mechanic mechanic, LocalDate date) {
        if (mechanic.isOff(date)) {
            throw new MechanicNotAvailableException("Mechanic is off at the requested date.");
        }
    }

    private void validateAppointmentDate(LocalDateTime startTime, LocalDateTime endTime) {
        if (endTime.isBefore(startTime)) {
            throw new InvalidAppointmentDateTimeException("End time is before start time");
        } else if (startTime.toLocalDate().isBefore(timeService.getToday())) {
            throw new InvalidAppointmentDateTimeException("Appointment date cannot be before today");
        }
    }

    private void validateOverlappingAppointments(Mechanic mechanic, LocalDateTime startTime, LocalDateTime endTime) {
        // Check for existing appointments that would overlap
        List<Appointment> overlappingAppointments = appointmentRepository.findByMechanicBetweenGivenSlot(
                mechanic.getId(), startTime, endTime);

        if (!overlappingAppointments.isEmpty()) {
            throw new MechanicNotAvailableException("Mechanic is not available at the requested time.");
        }
    }
}
