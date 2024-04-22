package garage.reservation.fastandfurious.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Mechanic mechanic;
    @Enumerated(EnumType.STRING)
    private GarageOperationType garageOperationType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    // half hour will be represented as 0.5 hour and calculations will be made
    private Double duration;
}
