package garage.reservation.fastandfurious.service;

import garage.reservation.fastandfurious.domain.Mechanic;
import garage.reservation.fastandfurious.repository.MechanicRepository;
import garage.reservation.fastandfurious.service.exception.MechanicNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MechanicService {

    private final MechanicRepository mechanicRepository;

    public MechanicService(MechanicRepository mechanicRepository) {
        this.mechanicRepository = mechanicRepository;
    }

    public Mechanic saveMechanic(Mechanic mechanic) {
        return mechanicRepository.save(mechanic);
    }

    public List<Mechanic> getAll() {
        return mechanicRepository.findAll();
    }

    public Mechanic getById(Long id) {
        return mechanicRepository.findById(id).orElseThrow( () -> new MechanicNotFoundException(id));
    }
}
