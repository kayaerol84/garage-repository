package garage.reservation.fastandfurious.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Mechanic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<DayOfWeek> offDays;

    public Mechanic(String name, Set<DayOfWeek> offDays) {
        this.name = name;
        this.offDays = offDays;
    }

    public boolean isOff(LocalDate date) {

        return this.offDays.contains(date.getDayOfWeek()) || date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }
}
