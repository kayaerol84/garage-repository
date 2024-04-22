package garage.reservation.fastandfurious.repository;

import garage.reservation.fastandfurious.domain.Mechanic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MechanicRepository extends JpaRepository<Mechanic, Long> {
}
