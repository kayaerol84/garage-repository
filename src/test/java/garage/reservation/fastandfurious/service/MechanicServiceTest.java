package garage.reservation.fastandfurious.service;

import garage.reservation.fastandfurious.repository.MechanicRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.DayOfWeek;
import java.util.Set;
import static garage.reservation.fastandfurious.domain.DomainTestHelper.buildMechanic;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MechanicServiceTest {

    @Mock
    MechanicRepository mechanicRepository;

    @InjectMocks
    MechanicService mechanicService;

    @Test
    void saveMechanicShouldReturnMechanic() {
        var mechanic = buildMechanic(1L, "First Mech", Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY));
        when(mechanicRepository.save(any())).thenReturn(mechanic);

        var result = mechanicService.saveMechanic(mechanic);

        assertEquals(result, mechanic);
    }

    @Test
    void getAll() {
    }

    @Test
    void getById() {
    }
}