package garage.reservation.fastandfurious.repository;

import garage.reservation.fastandfurious.domain.OperatingHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperatingHoursRepository extends JpaRepository<OperatingHours, Long> {
}
