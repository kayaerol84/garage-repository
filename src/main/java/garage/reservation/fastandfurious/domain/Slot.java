package garage.reservation.fastandfurious.domain;

import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;

@Value
@Builder
public class Slot {

    LocalDateTime startTime;
    LocalDateTime endTime;
}
