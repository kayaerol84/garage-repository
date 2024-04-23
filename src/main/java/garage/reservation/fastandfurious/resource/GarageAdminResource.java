package garage.reservation.fastandfurious.resource;

import garage.reservation.fastandfurious.domain.Mechanic;
import garage.reservation.fastandfurious.domain.OperatingHours;
import garage.reservation.fastandfurious.resource.dto.SaveMechanicRequest;
import garage.reservation.fastandfurious.resource.dto.OperatingHoursResponse;
import garage.reservation.fastandfurious.resource.dto.SaveOperatingHoursRequest;
import garage.reservation.fastandfurious.resource.dto.MechanicResponse;
import garage.reservation.fastandfurious.service.MechanicService;
import garage.reservation.fastandfurious.service.OperatingHoursService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/garage")
public class GarageAdminResource {
    private final MechanicService mechanicService;
    private final OperatingHoursService operatingHoursService;

    public GarageAdminResource(MechanicService mechanicService, OperatingHoursService operatingHoursService) {
        this.mechanicService = mechanicService;
        this.operatingHoursService = operatingHoursService;
    }

    @PostMapping("/mechanics")
    public ResponseEntity<MechanicResponse> createMechanic(@RequestBody SaveMechanicRequest request) {
        Mechanic mechanic = mechanicService.saveMechanic(request.toMechanic());
        return ResponseEntity.ok(MechanicResponse.from(mechanic));
    }

    @PostMapping("/mechanics/{id}")
    public ResponseEntity<MechanicResponse> updateMechanic(@PathVariable Long id, @RequestBody SaveMechanicRequest request) {
        if (mechanicService.getById(id) != null) {
            Mechanic mechanic = mechanicService.saveMechanic(request.toMechanic(id));
            return ResponseEntity.ok(MechanicResponse.from(mechanic));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/mechanics")
    public ResponseEntity<List<MechanicResponse>> getMechanics() {
        List<Mechanic> mechanics = mechanicService.getAll();
        return ResponseEntity
                .ok(mechanics.stream().map(MechanicResponse::from).collect(Collectors.toList()));
    }

    @GetMapping("/mechanics/{id}")
    public ResponseEntity<MechanicResponse> getMechanicsById(@PathVariable Long id) {
        Mechanic mechanic = mechanicService.getById(id);
        return ResponseEntity.ok(MechanicResponse.from(mechanic));
    }

    @PutMapping("/operatingHours/{id}")
    public ResponseEntity<OperatingHoursResponse> updateOperatingHours(@PathVariable Long id, @RequestBody SaveOperatingHoursRequest saveOperatingHoursRequest) {
        if (operatingHoursService.getAll().stream().anyMatch(o -> Objects.equals(o.getId(), id))) {
            OperatingHours operatingHours = operatingHoursService.saveOperatingHours(saveOperatingHoursRequest.toOperatingHours(id));
            return ResponseEntity.ok(OperatingHoursResponse.from(operatingHours));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/operatingHours")
    public ResponseEntity<OperatingHoursResponse> addSpecificOperatingHours(@RequestBody SaveOperatingHoursRequest saveOperatingHoursRequest) {
        OperatingHours operatingHours = operatingHoursService.saveOperatingHours(saveOperatingHoursRequest.toSpecificDateOperatingHours());

        return ResponseEntity.ok(OperatingHoursResponse.fromSpecificDate(operatingHours));
    }

    @GetMapping("/operatingHours")
    public ResponseEntity<List<OperatingHoursResponse>> getOperatingHours() {
        List<OperatingHours> operatingHours = operatingHoursService.getAll();

        return ResponseEntity.ok(operatingHours.stream().map(OperatingHoursResponse::from).collect(Collectors.toList()));
    }
}
