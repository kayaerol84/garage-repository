package garage.reservation.fastandfurious.service;

import garage.reservation.fastandfurious.domain.Appointment;
import garage.reservation.fastandfurious.domain.GarageOperationType;
import garage.reservation.fastandfurious.domain.Mechanic;
import garage.reservation.fastandfurious.domain.OperatingHours;
import garage.reservation.fastandfurious.domain.Slot;
import garage.reservation.fastandfurious.repository.AppointmentRepository;
import garage.reservation.fastandfurious.service.exception.InvalidAppointmentDateTimeException;
import garage.reservation.fastandfurious.service.exception.MechanicNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import static garage.reservation.fastandfurious.domain.DomainTestHelper.buildAppointment;
import static garage.reservation.fastandfurious.domain.DomainTestHelper.buildMechanic;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private MechanicService mechanicService;
    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private OperatingHoursService operatingHoursService;
    @Mock
    private AppointmentValidator appointmentValidator;
    
    @InjectMocks
    ReservationService reservationService;

    private final Long mechanicId = 1L;
    static LocalDate MONDAY_22_04 = LocalDate.of(2024, 4, 22);
    static LocalDateTime MONDAY_22_04_12_00 = LocalDateTime.of(2024, 4, 22, 12, 0);

    @BeforeEach
    void setUp() {
    }

    @Test
    void shouldFindSlotsWhenMechanicIsAvailable() {
        // when
        Mechanic mechanic = buildMechanic(1L, "Mech");
        OperatingHours operatingHours = OperatingHours.builder()
                .openTime(LocalTime.of(9, 0, 0))
                .closeTime(LocalTime.of(18, 0, 0))
                .dayOfWeek(DayOfWeek.MONDAY)
                .build();
        when(mechanicService.getById(mechanicId)).thenReturn(mechanic);
        when(operatingHoursService.findOperatingHoursForDate(MONDAY_22_04)).thenReturn(operatingHours);
        when(appointmentRepository.findByMechanicBetweenGivenSlot(mechanicId, LocalDateTime.of(MONDAY_22_04, operatingHours.getOpenTime()),
                LocalDateTime.of(MONDAY_22_04, operatingHours.getCloseTime()))).thenReturn(Collections.emptyList());

        // execute
        List<Slot> availableSlots = reservationService.findAvailableSlots(GarageOperationType.GENERAL_CHECK, mechanicId, MONDAY_22_04);

        // verify there are 3 available General Check slots in the 9 operating hours
        assertFalse(availableSlots.isEmpty());
        assertEquals(3, availableSlots.size());
    }

    @Test
    void shouldNotFindSlotsWhenMechanicIsOff() {
        Mechanic mechanic = Mechanic.builder().id(1L).offDays(Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY)).build();
        when(mechanicService.getById(mechanicId)).thenReturn(mechanic);

        List<Slot> availableSlots = reservationService.findAvailableSlots(GarageOperationType.GENERAL_CHECK, mechanicId, MONDAY_22_04);
        assertTrue(availableSlots.isEmpty());
    }

    @Test
    void shouldFindLessSlotsWhenMechanicAlreadyHasAppointments() {
        // when
        Mechanic mechanic = Mechanic.builder().id(1L).offDays(Set.of(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY)).build();
        OperatingHours operatingHours = OperatingHours.builder()
                .openTime(LocalTime.of(9, 0, 0))
                .closeTime(LocalTime.of(18, 0, 0))
                .dayOfWeek(DayOfWeek.MONDAY)
                .build();
        LocalDateTime appointmentStartTime = LocalDateTime.of(MONDAY_22_04, LocalTime.of(11, 0, 0));
        LocalDateTime appointmentEndTime = LocalDateTime.of(MONDAY_22_04, LocalTime.of(14, 0, 0));
        List<Appointment> appointments = List.of(Appointment.builder().mechanic(mechanic).startTime(appointmentStartTime).endTime(appointmentEndTime).build());

        when(mechanicService.getById(mechanicId)).thenReturn(mechanic);
        when(operatingHoursService.findOperatingHoursForDate(MONDAY_22_04)).thenReturn(operatingHours);
        when(appointmentRepository.findByMechanicBetweenGivenSlot(mechanicId, LocalDateTime.of(MONDAY_22_04, operatingHours.getOpenTime()),
                LocalDateTime.of(MONDAY_22_04, operatingHours.getCloseTime()))).thenReturn(appointments);

        // execute
        List<Slot> availableSlots = reservationService.findAvailableSlots(GarageOperationType.GENERAL_CHECK, mechanicId, MONDAY_22_04);
        assertFalse(availableSlots.isEmpty());

        // verify there is only 1 available General Check slot in the 9 operating hours because there is an existing appointment in the middle of the day
        assertEquals(1, availableSlots.size());
    }

    @Test
    void createAppointmentShouldSuccessfullyBookTimeSlotForMechanic(){
        Mechanic expectedMechanic = buildMechanic(1L, "First Mech");
        when(mechanicService.getById(mechanicId)).thenReturn(expectedMechanic);
        var expectedTime = MONDAY_22_04_12_00;
        Appointment expectedAppointment = buildAppointment(expectedTime, GarageOperationType.GENERAL_CHECK, expectedMechanic);
        when(appointmentRepository.save(any())).thenReturn(expectedAppointment);
        doNothing().when(appointmentValidator).validate(any(), any(), any());

        // execute
        Appointment appointment = reservationService.createAppointment(mechanicId, GarageOperationType.GENERAL_CHECK, expectedTime);

        // verify
        assertEquals(expectedAppointment, appointment);
    }

    @Test
    void createAppointmentShouldFailWhenValidationThrowsException(){

        Mechanic expectedMechanic = buildMechanic(1L, "First Mech");
        when(mechanicService.getById(mechanicId)).thenReturn(expectedMechanic);
        var expectedTime = MONDAY_22_04_12_00;

        doThrow(InvalidAppointmentDateTimeException.class).when(appointmentValidator).validate(any(), any(), any());

        // execute
        assertThrows( InvalidAppointmentDateTimeException.class, () -> reservationService.createAppointment(mechanicId, GarageOperationType.GENERAL_CHECK, expectedTime));
    }

    @Test
    void createAppointmentShouldFailWhenMechanicNotFound(){
        // when
        when(mechanicService.getById(mechanicId)).thenThrow(new MechanicNotFoundException(mechanicId));

        // execute
        assertThrows(MechanicNotFoundException.class, () -> reservationService.createAppointment(mechanicId, GarageOperationType.BROKEN_LAMP_CHANGE, MONDAY_22_04_12_00));
    }
}