package garage.reservation.fastandfurious.service;

import garage.reservation.fastandfurious.domain.OperatingHours;
import garage.reservation.fastandfurious.repository.OperatingHoursRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import static garage.reservation.fastandfurious.domain.DomainTestHelper.buildOperatingHours;
import static garage.reservation.fastandfurious.domain.DomainTestHelper.buildOperatingHoursList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OperatingHoursServiceTest {

    @Mock
    OperatingHoursRepository operatingHoursRepository;

    @InjectMocks
    OperatingHoursService operatingHoursService;

    LocalDate MONDAY = LocalDate.of(2024, 4, 22);

    @Test
    void findOperatingHoursForDateShouldReturnCorrectDayDetails() {

        when(operatingHoursRepository.findAll()).thenReturn(buildOperatingHoursList());

        OperatingHours result = operatingHoursService.findOperatingHoursForDate(MONDAY);

        assertEquals(DayOfWeek.MONDAY, result.getDayOfWeek());
        assertEquals(DayOfWeek.TUESDAY, operatingHoursService.findOperatingHoursForDate(MONDAY.plusDays(1)).getDayOfWeek());
    }

    @Test
    void saveOperatingHoursShouldReturnOperatingHours() {
        var operatingHours = buildOperatingHours(DayOfWeek.TUESDAY, LocalTime.of(8, 0), LocalTime.of(18, 0));
        when(operatingHoursRepository.save(any())).thenReturn(operatingHours);

        OperatingHours result = operatingHoursService.saveOperatingHours(operatingHours);

        assertEquals(operatingHours.getOpenTime(), result.getOpenTime());
        assertEquals(operatingHours.getCloseTime(), result.getCloseTime());
        assertEquals(operatingHours.getDayOfWeek(), result.getDayOfWeek());
    }

}