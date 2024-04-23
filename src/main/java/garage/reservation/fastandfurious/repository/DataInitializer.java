package garage.reservation.fastandfurious.repository;

import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import garage.reservation.fastandfurious.domain.Mechanic;
import garage.reservation.fastandfurious.domain.OperatingHours;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Component
public class DataInitializer {

    private final OperatingHoursRepository operatingHoursRepository;
    private final MechanicRepository mechanicRepository;

    public DataInitializer(OperatingHoursRepository operatingHoursRepository, MechanicRepository mechanicRepository) {
        this.operatingHoursRepository = operatingHoursRepository;
        this.mechanicRepository = mechanicRepository;
    }

    @PostConstruct
    public void initData() {
        initMechanics();
        initOperatingHours();
    }

    private void initMechanics() {
        mechanicRepository.save(Mechanic.builder().name("First Mech").offDays(Set.of(DayOfWeek.TUESDAY)).build());
        mechanicRepository.save(Mechanic.builder().name("Second Mech").offDays(Set.of(DayOfWeek.MONDAY)).build());
    }

    private void initOperatingHours() {
        LocalTime openingTime = LocalTime.of(8, 0);
        LocalTime closeTime = LocalTime.of(18, 0);
        List<OperatingHours> entities = List.of(
                buildOperatingHours(DayOfWeek.MONDAY, openingTime, closeTime),
                buildOperatingHours(DayOfWeek.TUESDAY, openingTime, closeTime),
                buildOperatingHours(DayOfWeek.WEDNESDAY, openingTime, closeTime),
                buildOperatingHours(DayOfWeek.THURSDAY, openingTime, closeTime),
                buildOperatingHours(DayOfWeek.FRIDAY, openingTime, closeTime),
                buildOperatingHours(DayOfWeek.SATURDAY, openingTime.plusHours(4), closeTime.minusHours(1)),
                buildOperatingHours(DayOfWeek.SUNDAY, openingTime.minusHours(8), closeTime.minusHours(18))
        );
        operatingHoursRepository.saveAll(entities);
    }

    private static OperatingHours buildOperatingHours(DayOfWeek day, LocalTime openingTime, LocalTime closeTime) {
        return OperatingHours.builder()
                .dayOfWeek(day)
                .openTime(openingTime)
                .closeTime(closeTime)
                .build();
    }
}
