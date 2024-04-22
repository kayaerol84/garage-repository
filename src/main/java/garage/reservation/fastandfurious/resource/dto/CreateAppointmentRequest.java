package garage.reservation.fastandfurious.resource.dto;

import lombok.Builder;
import lombok.Value;
import org.springframework.validation.annotation.Validated;
import java.time.LocalDateTime;

@Value
@Builder
public class CreateAppointmentRequest {
    Long mechanicId;
    String jobType;
    LocalDateTime startTime;
}
