package garage.reservation.fastandfurious.resource.dto;

import garage.reservation.fastandfurious.domain.Appointment;
import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;

@Value
@Builder
public class AppointmentResponse {
    Long id;
    Long mechanicId;
    String garageOperationType;
    LocalDateTime startTime;
    LocalDateTime endTime;

    public static AppointmentResponse from(Appointment appointment) {
        return AppointmentResponse.builder()
                .garageOperationType(appointment.getGarageOperationType().name())
                .id(appointment.getId())
                .mechanicId(appointment.getMechanic().getId())
                .startTime(appointment.getStartTime())
                .endTime(appointment.getEndTime())
                .build();
    }
}
