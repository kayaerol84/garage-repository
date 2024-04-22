package garage.reservation.fastandfurious.resource.dto;

import garage.reservation.fastandfurious.domain.Slot;
import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;

@Value
@Builder
public class AvailableSlotResponse {
    LocalDateTime startTime;
    LocalDateTime endTime;

    public static AvailableSlotResponse fromSlot(Slot slot) {
        return AvailableSlotResponse.builder()
                .startTime(slot.getStartTime())
                .endTime(slot.getEndTime())
                .build();
    }
}
