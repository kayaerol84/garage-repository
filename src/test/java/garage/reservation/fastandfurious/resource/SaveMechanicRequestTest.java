package garage.reservation.fastandfurious.resource;

import garage.reservation.fastandfurious.domain.Mechanic;
import garage.reservation.fastandfurious.resource.dto.SaveMechanicRequest;
import garage.reservation.fastandfurious.resource.exception.InvalidOffDayException;
import org.junit.jupiter.api.Test;
import java.time.DayOfWeek;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class SaveMechanicRequestTest {

    @Test
    void toMechanicShouldFailWhenInvalidOffDayProvided() {
        SaveMechanicRequest request = new SaveMechanicRequest("name", Set.of("MONDAY", "TUUUESDAY"));
        assertThrows(InvalidOffDayException.class, request::toMechanic);
    }

    @Test
    void toMechanicShouldSucceed() {
        SaveMechanicRequest request = new SaveMechanicRequest("name", Set.of("MONDAY", "TUESDAY"));
        Mechanic mechanic = request.toMechanic();
        assertTrue(mechanic.getOffDays().contains(DayOfWeek.MONDAY));
        assertTrue(mechanic.getOffDays().contains(DayOfWeek.TUESDAY));
        assertFalse(mechanic.getOffDays().contains(DayOfWeek.WEDNESDAY));
    }
}