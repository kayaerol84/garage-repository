package garage.reservation.fastandfurious.service;

import garage.reservation.fastandfurious.domain.OperatingHours;
import garage.reservation.fastandfurious.repository.OperatingHoursRepository;
import garage.reservation.fastandfurious.service.exception.OperatingHoursNotFoundException;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OperatingHoursService {
    private final OperatingHoursRepository operatingHoursRepository;

    public OperatingHoursService(OperatingHoursRepository operatingHoursRepository) {
        this.operatingHoursRepository = operatingHoursRepository;
    }

    public OperatingHours findOperatingHoursForDate(LocalDate date) {
        List<OperatingHours> operatingHoursList = operatingHoursRepository.findAll();

        Optional<OperatingHours> operatingHoursOnSpecificDate = operatingHoursList.stream().filter(o -> o.getSpecificDate() == date).findFirst();
        return operatingHoursOnSpecificDate.orElse(getOperatingHourForWeekDays(operatingHoursList, date));
    }

    private OperatingHours getOperatingHourForWeekDays(List<OperatingHours> operatingHoursList, LocalDate date) {
        return operatingHoursList.stream()
                .filter(operatingHours -> operatingHours.getDayOfWeek() == date.getDayOfWeek())
                .findFirst()
                .orElseThrow(() -> new OperatingHoursNotFoundException(date));
    }

    public OperatingHours updateOperatingHours() {
        return  null;
    }

    public List<OperatingHours> getAll() {
        return operatingHoursRepository.findAll();
    }
}
