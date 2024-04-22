package garage.reservation.fastandfurious.resource.dto;

import lombok.Builder;
import lombok.Value;
import java.util.List;

@Value
@Builder
public class AvailableSlotsResponse {
    List<AvailableSlotResponse> availableSlots;
}


