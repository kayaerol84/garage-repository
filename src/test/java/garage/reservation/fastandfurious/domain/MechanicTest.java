package garage.reservation.fastandfurious.domain;

import org.junit.jupiter.api.Test;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class MechanicTest {

    private static final LocalDate SUNDAY = LocalDate.of(2024, 4, 21);
    private static final LocalDate TUESDAY = LocalDate.of(2024, 4, 23);
    private static final LocalDate WEDNESDAY = LocalDate.of(2024, 4, 24);

    @Test
    void isOffShouldReturnTrueForGivenOffDays() {

        Mechanic mechanic = new Mechanic(1L, "name", Set.of(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY));
        assertTrue(mechanic.isOff(SUNDAY));
        assertTrue(mechanic.isOff(TUESDAY));
        assertTrue(mechanic.isOff(WEDNESDAY));
    }

    @Test
    void isOffShouldReturnFalseForGivenWorkingDays() {

        Mechanic mechanic = new Mechanic(1L, "name", Set.of(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY));
        assertFalse(mechanic.isOff(SUNDAY.minusDays(1)));
        assertFalse(mechanic.isOff(WEDNESDAY.plusDays(1)));
    }
}