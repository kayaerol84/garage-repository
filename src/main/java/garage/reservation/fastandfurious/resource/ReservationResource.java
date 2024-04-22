package garage.reservation.fastandfurious.resource;

import garage.reservation.fastandfurious.domain.Appointment;
import garage.reservation.fastandfurious.domain.GarageOperationType;
import garage.reservation.fastandfurious.resource.dto.AppointmentResponse;
import garage.reservation.fastandfurious.resource.dto.AvailableSlotResponse;
import garage.reservation.fastandfurious.resource.dto.AvailableSlotsResponse;
import garage.reservation.fastandfurious.resource.dto.CreateAppointmentRequest;
import garage.reservation.fastandfurious.service.ReservationService;
import garage.reservation.fastandfurious.service.TimeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservations")
public class ReservationResource {

    private final ReservationService reservationService;
    private final TimeService timeService;

    public ReservationResource(ReservationService reservationService, TimeService timeService) {
        this.reservationService = reservationService;
        this.timeService = timeService;
    }

    @GetMapping("/slots")
    public ResponseEntity<AvailableSlotsResponse> getAvailableSlots(
            @RequestParam String jobType,
            @RequestParam Long mechanicId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        if (date.isBefore(timeService.getToday())) {
            return ResponseEntity.ok(AvailableSlotsResponse.builder().build());
        }

        // TODO handle invalid jobType
        GarageOperationType job = GarageOperationType.valueOf(jobType);

        var availableSlots = reservationService.findAvailableSlots(job, mechanicId, date)
                .stream().map(AvailableSlotResponse::fromSlot).collect(Collectors.toList());

        return ResponseEntity.ok(AvailableSlotsResponse.builder().availableSlots(availableSlots).build());
    }

    @PostMapping("/appointments")
    public ResponseEntity<AppointmentResponse> createReservation(@RequestBody CreateAppointmentRequest request) {

        // Customer that creates the appointment is ignored.
        var jobType = GarageOperationType.valueOf(request.getJobType());
        Appointment appointment = reservationService.createAppointment(request.getMechanicId(), jobType, request.getStartTime());
        return ResponseEntity.ok(AppointmentResponse.from(appointment));
    }
}
