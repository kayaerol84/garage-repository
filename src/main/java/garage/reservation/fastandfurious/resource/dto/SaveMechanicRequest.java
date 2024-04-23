package garage.reservation.fastandfurious.resource.dto;

import garage.reservation.fastandfurious.domain.Mechanic;
import garage.reservation.fastandfurious.resource.exception.InvalidOffDayException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import java.time.DayOfWeek;
import java.util.Set;
import java.util.stream.Collectors;

@Value
@Builder
@AllArgsConstructor
public class SaveMechanicRequest {
    String name;
    Set<String> offDays;

    public Mechanic toMechanic() {
        try {
            Set<DayOfWeek> offDaysSet = offDays.stream().map(DayOfWeek::valueOf).collect(Collectors.toSet());
            return new Mechanic(name, offDaysSet);
        } catch (IllegalArgumentException e) {
            throw new InvalidOffDayException();
        }
    }

    public Mechanic toMechanic(Long id) {
        try {
            Set<DayOfWeek> offDaysSet = offDays.stream().map(DayOfWeek::valueOf).collect(Collectors.toSet());
            return new Mechanic(id, name, offDaysSet);
        } catch (IllegalArgumentException e) {
            throw new InvalidOffDayException();
        }
    }
}
