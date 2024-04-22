package garage.reservation.fastandfurious.resource.dto;

import garage.reservation.fastandfurious.domain.Mechanic;
import garage.reservation.fastandfurious.resource.exception.InvalidOffDayException;
import lombok.Value;
import java.time.DayOfWeek;
import java.util.Set;
import java.util.stream.Collectors;

@Value
public class CreateMechanicRequest {
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
}
