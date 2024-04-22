package garage.reservation.fastandfurious.resource.dto;

import garage.reservation.fastandfurious.domain.Mechanic;
import lombok.Builder;
import lombok.Value;
import java.util.Set;
import java.util.stream.Collectors;

@Value
@Builder
public class MechanicResponse {
    Long id;
    String name;
    Set<String> offDays;

    public static MechanicResponse from(Mechanic mechanic) {
        return MechanicResponse.builder()
                .id(mechanic.getId())
                .name(mechanic.getName())
                .offDays(mechanic.getOffDays().stream().map(Enum::name).collect(Collectors.toSet()))
                .build();
    }
}
