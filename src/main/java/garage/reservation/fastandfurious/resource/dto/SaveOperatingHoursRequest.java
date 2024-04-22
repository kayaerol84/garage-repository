package garage.reservation.fastandfurious.resource.dto;

import lombok.Builder;
import lombok.Value;
import java.time.LocalDate;
import java.time.LocalTime;

@Value
@Builder
public class SaveOperatingHoursRequest {
    Long id;
    String dayOfWeek;
    LocalDate specificDate;
    LocalTime openTime;
    LocalTime closeTime;
}
