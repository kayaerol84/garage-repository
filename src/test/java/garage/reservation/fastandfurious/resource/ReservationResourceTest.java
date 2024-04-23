package garage.reservation.fastandfurious.resource;

import garage.reservation.fastandfurious.domain.Appointment;
import garage.reservation.fastandfurious.domain.GarageOperationType;
import garage.reservation.fastandfurious.domain.Slot;
import garage.reservation.fastandfurious.resource.dto.AppointmentResponse;
import garage.reservation.fastandfurious.resource.dto.AvailableSlotResponse;
import garage.reservation.fastandfurious.resource.dto.AvailableSlotsResponse;
import garage.reservation.fastandfurious.resource.dto.CreateAppointmentRequest;
import garage.reservation.fastandfurious.service.ReservationService;
import garage.reservation.fastandfurious.service.TimeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import static garage.reservation.fastandfurious.domain.DomainTestHelper.buildAppointment;
import static garage.reservation.fastandfurious.domain.DomainTestHelper.buildMechanic;
import static garage.reservation.fastandfurious.domain.DomainTestHelper.buildSlot;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationResourceTest {

    @Mock
    private ReservationService reservationService;
    @Mock
    private TimeService timeService;

    @InjectMocks
    ReservationResource reservationResource;

    private final Long mechanicId = 1L;
    private final LocalDate MONDAY_15_04 = LocalDate.of(2024, 4, 22);
    private final LocalDate MONDAY_22_04 = LocalDate.of(2024, 4, 22);

    @Test
    void getAvailableSlotsShouldNotReturnAnyAvailableSlotForThePastDates() {
        // given
        when(timeService.getToday()).thenReturn(MONDAY_22_04);
        LocalDateTime startTime = LocalDateTime.of(MONDAY_15_04, LocalTime.of(12, 0));
        assertTrue(startTime.isBefore(LocalDateTime.now()));

        List<Slot> availableSlots = List.of(buildSlot(startTime, GarageOperationType.GENERAL_CHECK));
        when(reservationService.findAvailableSlots(GarageOperationType.GENERAL_CHECK, mechanicId, MONDAY_22_04)).thenReturn(availableSlots);

        //execute
        AvailableSlotsResponse slots = reservationResource.getAvailableSlots("GENERAL_CHECK", mechanicId, MONDAY_22_04).getBody();

        assertNotNull(slots);
        assertEquals(1, slots.getAvailableSlots().size());
    }

    @Test
    void getAvailableSlotsShouldReturnAvailableSlotsForTheDate() {
        // given
        when(timeService.getToday()).thenReturn(MONDAY_22_04);
        LocalDateTime startTime = LocalDateTime.of(MONDAY_22_04, LocalTime.of(12, 0));
        List<Slot> availableSlots = List.of(buildSlot(startTime, GarageOperationType.GENERAL_CHECK));
        when(reservationService.findAvailableSlots(GarageOperationType.GENERAL_CHECK, mechanicId, MONDAY_22_04)).thenReturn(availableSlots);

        //execute
        ResponseEntity<AvailableSlotsResponse> slots = reservationResource.getAvailableSlots("GENERAL_CHECK", mechanicId, MONDAY_22_04);

        // verify
        List<AvailableSlotResponse> result = slots.getBody().getAvailableSlots();
        assertFalse(result.isEmpty());
        assertEquals(startTime, result.get(0).getStartTime());
        assertEquals(startTime.plusHours((long) GarageOperationType.GENERAL_CHECK.getDurationHours()), result.get(0).getEndTime());
    }

    @Test
    void createReservationShouldReturnAppointment() {

        // given
        LocalDateTime startTime = LocalDateTime.of(MONDAY_22_04, LocalTime.of(12, 0));
        var request = CreateAppointmentRequest.builder()
                .mechanicId(mechanicId)
                .startTime(startTime)
                .jobType("GENERAL_CHECK")
                .build();

        Appointment appointment = buildAppointment(startTime, GarageOperationType.GENERAL_CHECK, buildMechanic(mechanicId, "Mech"));

        when(reservationService.createAppointment(mechanicId, GarageOperationType.GENERAL_CHECK, startTime)).thenReturn(appointment);

        // execute
        AppointmentResponse result = reservationResource.createReservation(request).getBody();

        // verify
        assertNotNull(result);
        assertEquals(GarageOperationType.GENERAL_CHECK.name(), result.getGarageOperationType());
        assertEquals(mechanicId, result.getMechanicId());
        assertEquals(startTime.plusHours((long) GarageOperationType.GENERAL_CHECK.getDurationHours()), result.getEndTime());
    }
}