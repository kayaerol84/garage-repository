package garage.reservation.fastandfurious.service;

import garage.reservation.fastandfurious.domain.Appointment;
import garage.reservation.fastandfurious.domain.GarageOperationType;
import garage.reservation.fastandfurious.domain.Mechanic;
import garage.reservation.fastandfurious.repository.AppointmentRepository;
import garage.reservation.fastandfurious.service.exception.MechanicNotAvailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import static garage.reservation.fastandfurious.domain.DomainTestHelper.buildAppointment;
import static garage.reservation.fastandfurious.domain.DomainTestHelper.buildMechanic;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentValidatorTest {

    @Mock
    AppointmentRepository appointmentRepository;

    @Mock
    TimeService timeService;

    @InjectMocks
    AppointmentValidator appointmentValidator;

    static LocalDateTime MONDAY_12_00 = LocalDateTime.of(2024, 4, 22, 12, 0, 0);
    static LocalDateTime MONDAY_14_00 = LocalDateTime.of(2024, 4, 22, 14, 0, 0);

    @BeforeEach
    void setup() {
        when(timeService.getToday()).thenReturn(MONDAY_12_00.toLocalDate());
    }

    @Test
    void validateShouldThrowExceptionWhenThereAreOverlappingAppointments() {
        Mechanic mechanic = buildMechanic("test");
        List<Appointment> appointments = List.of(buildAppointment(MONDAY_12_00, GarageOperationType.GENERAL_CHECK, mechanic));

        when(appointmentRepository.findByMechanicBetweenGivenSlot(mechanic.getId(), MONDAY_12_00, MONDAY_14_00)).thenReturn(appointments);
        assertThrows(MechanicNotAvailableException.class, () -> appointmentValidator.validate(mechanic, MONDAY_12_00, MONDAY_14_00 ));
    }

    @Test
    void validateShouldNotThrowExceptionWhenThereAreNoOverlappingAppointments() {
        Mechanic mechanic = buildMechanic("test");

        when(appointmentRepository.findByMechanicBetweenGivenSlot(mechanic.getId(), MONDAY_12_00, MONDAY_14_00)).thenReturn(Collections.emptyList());
        assertDoesNotThrow(() -> appointmentValidator.validate(mechanic, MONDAY_12_00, MONDAY_14_00 ));
    }
}