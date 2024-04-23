package garage.reservation.fastandfurious.domain;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.DayOfWeek.THURSDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;

public class DomainTestHelper {

    public static Mechanic buildMechanic(String name) {
        return Mechanic.builder()
                .name(name)
                .offDays(Set.of(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY))
                .build();
    }

    public static Mechanic buildMechanic(Long id, String name) {
        return buildMechanic(id, name, Set.of(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY));
    }

    public static Mechanic buildMechanic(Long id, String name, Set<DayOfWeek> offDays) {
        return Mechanic.builder()
                .id(id)
                .name(name)
                .offDays(offDays)
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

    public static OperatingHours buildOperatingHours(Long id, DayOfWeek day, LocalTime openingTime, LocalTime closeTime) {
        return OperatingHours.builder()
                .id(id)
                .dayOfWeek(day)
                .openTime(openingTime)
                .closeTime(closeTime)
                .build();

    }

    public static OperatingHours buildOperatingHours(DayOfWeek day, LocalTime openingTime, LocalTime closeTime) {
        return buildOperatingHours(1L, day, openingTime, closeTime);
    }

    public static List<OperatingHours> buildOperatingHoursList() {
        var openingTime = LocalTime.of(8,0);
        var closingTime = LocalTime.of(18,0);
        return List.of(
                buildOperatingHours(1L, MONDAY, openingTime, closingTime),
                buildOperatingHours(2L, TUESDAY, openingTime, closingTime),
                buildOperatingHours(3L, WEDNESDAY, openingTime, closingTime),
                buildOperatingHours(4L, THURSDAY, openingTime, closingTime),
                buildOperatingHours(5L, FRIDAY, openingTime, closingTime),
                buildOperatingHours(6L, SATURDAY, openingTime.plusHours(4), closingTime),
                buildOperatingHours(7L, SUNDAY, openingTime.minusHours(8), closingTime.minusHours(18))
        );
    }

    public static OperatingHours buildOperatingHoursForSpecificDate(Long id, LocalDate specificDate, LocalTime openingTime, LocalTime closeTime) {
        return OperatingHours.builder()
                .id(id)
                .specificDate(specificDate)
                .openTime(openingTime)
                .closeTime(closeTime)
                .build();

    }

}
