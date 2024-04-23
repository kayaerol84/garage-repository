package garage.reservation.fastandfurious.repository;

import garage.reservation.fastandfurious.domain.Appointment;
import garage.reservation.fastandfurious.domain.GarageOperationType;
import garage.reservation.fastandfurious.domain.Mechanic;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import static garage.reservation.fastandfurious.domain.DomainTestHelper.buildAppointment;
import static garage.reservation.fastandfurious.domain.DomainTestHelper.buildMechanic;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DataJpaTest
class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private MechanicRepository mechanicRepository;

    private Mechanic mechanic;

    static LocalDateTime MONDAY_12_00 = LocalDateTime.of(2024, 4, 22, 12, 0, 0);
    static LocalDateTime MONDAY_14_00 = LocalDateTime.of(2024, 4, 22, 14, 0, 0);

    @BeforeEach
    public void setUp() {
        mechanic = buildMechanic("mechanic name");
        mechanicRepository.save(mechanic);
        // Initialize test data before each test method
        Appointment appointment = buildAppointment(MONDAY_12_00, GarageOperationType.GENERAL_CHECK, mechanic);
        appointmentRepository.save(appointment);
    }

    @AfterEach
    public void tearDown() {
        // Release test data after each test method
        appointmentRepository.deleteAll();
        mechanicRepository.deleteAll();
    }

    @ParameterizedTest(name = "Finding appointments between {0} and {1} returns appointments: {2}")
    @MethodSource("getAppointmentCases")
    void findByMechanicBetweenGivenSlot(LocalDateTime startDate, LocalDateTime endDate, boolean found) {
        List<Appointment> result = appointmentRepository.findByMechanicBetweenGivenSlot(mechanic.getId(), startDate, endDate);
        assertEquals(found, !result.isEmpty());
    }

    static List<Arguments> getAppointmentCases() {

        return Arrays.asList(
                arguments(MONDAY_12_00, MONDAY_14_00, true),
                arguments(MONDAY_12_00.minusHours(2), MONDAY_12_00.minusHours(1), false),
                arguments(MONDAY_12_00.minusHours(2), MONDAY_12_00.minusMinutes(1), false),
                arguments(MONDAY_12_00.plusHours(1), MONDAY_12_00.plusHours(4), true),
                arguments(MONDAY_12_00.plusHours(1), MONDAY_12_00.plusHours(2), true)

        );
    }

}