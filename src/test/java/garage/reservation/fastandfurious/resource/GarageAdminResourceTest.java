package garage.reservation.fastandfurious.resource;

import garage.reservation.fastandfurious.domain.Mechanic;
import garage.reservation.fastandfurious.domain.OperatingHours;
import garage.reservation.fastandfurious.resource.dto.OperatingHoursResponse;
import garage.reservation.fastandfurious.resource.dto.SaveMechanicRequest;
import garage.reservation.fastandfurious.resource.dto.MechanicResponse;
import garage.reservation.fastandfurious.resource.dto.SaveOperatingHoursRequest;
import garage.reservation.fastandfurious.service.MechanicService;
import garage.reservation.fastandfurious.service.OperatingHoursService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static garage.reservation.fastandfurious.domain.DomainTestHelper.buildMechanic;
import static garage.reservation.fastandfurious.domain.DomainTestHelper.buildOperatingHours;
import static garage.reservation.fastandfurious.domain.DomainTestHelper.buildOperatingHoursForSpecificDate;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.TUESDAY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GarageAdminResourceTest {
    @InjectMocks
    GarageAdminResource garageAdminResource;
    @Mock
    private MechanicService mechanicService;
    @Mock
    private OperatingHoursService operatingHoursService;

    @Test
    void createMechanicShouldReturnMechanicDetails() {
        var expectedMechanic = buildMechanic("First Mech");
        when(mechanicService.saveMechanic(expectedMechanic)).thenReturn(expectedMechanic);

        SaveMechanicRequest request = SaveMechanicRequest.builder().name(expectedMechanic.getName()).offDays(Set.of("TUESDAY", "WEDNESDAY")).build();
        MechanicResponse result = garageAdminResource.createMechanic(request).getBody();

        // verify
        assertNotNull(result);
        assertEquals(expectedMechanic.getName(), result.getName());
        assertEquals(expectedMechanic.getOffDays().stream().map(Enum::name).collect(Collectors.toSet()), result.getOffDays());
    }

    @Test
    void updateMechanicShouldUpdateTheMechanicDetails() {

        var id = 1L;
        var originalMechanic = buildMechanic(id, "First Mech");
        when(mechanicService.getById(id)).thenReturn(originalMechanic);
        var expectedMechanic = buildMechanic(id, "First Mech", Set.of(TUESDAY, DayOfWeek.THURSDAY));
        when(mechanicService.saveMechanic(expectedMechanic)).thenReturn(expectedMechanic);

        SaveMechanicRequest request = SaveMechanicRequest.builder().name(originalMechanic.getName()).offDays(Set.of("TUESDAY", "THURSDAY")).build();
        MechanicResponse result = garageAdminResource.updateMechanic(id, request).getBody();

        // verify
        assertNotNull(result);
        assertEquals(expectedMechanic.getName(), result.getName());
        assertEquals(expectedMechanic.getOffDays().stream().map(Enum::name).collect(Collectors.toSet()), result.getOffDays());
    }

    @Test
    void getMechanicsShouldReturnAllMechanics() {
        List<Mechanic> mechanics = List.of(buildMechanic(1L, "First Mech"), buildMechanic(2L, "Second Mech"));
        when(mechanicService.getAll()).thenReturn(mechanics);

        List<MechanicResponse> result = garageAdminResource.getMechanics().getBody();

        // verify
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getMechanicsByIdShouldFindTheMechanicById() {
        Mechanic mechanic = buildMechanic(2L, "Second Mech");
        when(mechanicService.getById(2L)).thenReturn(mechanic);

        MechanicResponse result = garageAdminResource.getMechanicsById(2L).getBody();

        // verify
        assertNotNull(result);
        assertEquals("Second Mech", result.getName());
    }

    @Test
    void updateOperatingHoursShouldReturnChanges() {

        // given
        LocalTime openingTime = LocalTime.of(8, 0, 0);
        LocalTime closingTime = LocalTime.of(18, 0, 0);
        List<OperatingHours> operatingHoursList = List.of(
                buildOperatingHours(1L, MONDAY, openingTime, closingTime),
                buildOperatingHours(2L, TUESDAY, openingTime.plusHours(1), closingTime),
                buildOperatingHours(3L, SATURDAY, openingTime.plusHours(1), closingTime)
        );
        when(operatingHoursService.getAll()).thenReturn(operatingHoursList);
        SaveOperatingHoursRequest request = SaveOperatingHoursRequest.builder()
                .closeTime(closingTime.minusHours(1))
                .openTime(openingTime)
                .dayOfWeek("MONDAY")
                .build();
        Long id = 1L;
        OperatingHours updatedOpeningHours = buildOperatingHours(id, MONDAY, openingTime, closingTime.minusHours(1));
        when(operatingHoursService.saveOperatingHours(request.toOperatingHours(id))).thenReturn(updatedOpeningHours);

        // execute
        OperatingHoursResponse result = garageAdminResource.updateOperatingHours(id, request).getBody();

        // verify
        assertNotNull(result);
        assertTrue(result.getIsOn());
        assertEquals(updatedOpeningHours.getCloseTime(), result.getClosingTime());
        assertEquals(updatedOpeningHours.getOpenTime(), result.getOpeningTime());
    }

    @Test
    void addSpecificOperatingHoursShouldReturnNewOperatingHours() {

        LocalTime openingTime = LocalTime.of(8, 0, 0);
        LocalTime closingTime = LocalTime.of(18, 0, 0);
        LocalDate specificDate = LocalDate.of(2024, 5, 6);

        SaveOperatingHoursRequest request = SaveOperatingHoursRequest.builder()
                .closeTime(closingTime)
                .openTime(openingTime)
                .specificDate(specificDate)
                .build();

        Long id = 1L;

        OperatingHours createdOperatingHours = buildOperatingHoursForSpecificDate(id, specificDate, openingTime, closingTime.minusHours(1));

        when(operatingHoursService.saveOperatingHours(request.toSpecificDateOperatingHours())).thenReturn(createdOperatingHours);

        // execute
        OperatingHoursResponse result = garageAdminResource.addSpecificOperatingHours(request).getBody();

        // verify
        assertNotNull(result);
        assertEquals(createdOperatingHours.getSpecificDate(), result.getSpecificDate());
    }

    @Test
    void getOperatingHoursShouldReturnAll() {
        // given
        LocalTime openingTime = LocalTime.of(8, 0, 0);
        LocalTime closingTime = LocalTime.of(18, 0, 0);
        LocalDate specificDate = LocalDate.of(2024, 5, 7);
        List<OperatingHours> operatingHoursList = List.of(
                buildOperatingHours(1L, MONDAY, openingTime, closingTime),
                buildOperatingHours(2L, TUESDAY, openingTime.plusHours(1), closingTime),
                buildOperatingHours(3L, SATURDAY, openingTime.plusHours(1), closingTime),
                buildOperatingHoursForSpecificDate(3L, specificDate, openingTime.minusHours(8), closingTime.minusHours(18))
        );

        when(operatingHoursService.getAll()).thenReturn(operatingHoursList);

        // execute
        List<OperatingHoursResponse> result = garageAdminResource.getOperatingHours().getBody();

        // verify
        assertNotNull(result);
        assertEquals(4, result.size());
    }
}