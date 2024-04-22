package garage.reservation.fastandfurious.repository;

import garage.reservation.fastandfurious.domain.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a WHERE a.mechanic.id = ?1 " +
            "AND ( " +
            "(a.startTime <= ?2 AND a.endTime >= ?3) " +
            "OR (a.startTime < ?2 AND a.endTime > ?2) " +
            "OR (a.startTime < ?3 AND a.endTime < ?3))")
    List<Appointment> findByMechanicBetweenGivenSlot(Long mechanicId, LocalDateTime startDate, LocalDateTime endDate);
}
