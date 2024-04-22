package garage.reservation.fastandfurious.domain;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Set;

public class DomainTestHelper {

    public static Mechanic buildMechanic(String name) {
        return Mechanic.builder()
                .name(name)
                .offDays(Set.of(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY))
                .build();
    }

    public static Mechanic buildMechanic(Long id, String name) {
        return Mechanic.builder()
                .id(id)
                .name(name)
                .offDays(Set.of(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY))
                .build();
    }

    public static Appointment buildAppointment(LocalDateTime startTime, GarageOperationType operationType, Mechanic mechanic) {
        return Appointment.builder()
                .startTime(startTime)
                .garageOperationType(operationType)
                .endTime(startTime.plusHours((long) operationType.getDurationHours()))
                .duration(GarageOperationType.GENERAL_CHECK.getDurationHours())
                .mechanic(mechanic)
                .build();
    }

    public static Slot buildSlot(LocalDateTime startTime, GarageOperationType operationType) {
        return Slot.builder()
                .startTime(startTime)
                .endTime(startTime.plusHours((long) operationType.getDurationHours()))
                .build();
    }

}
