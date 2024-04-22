package garage.reservation.fastandfurious.service;

import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class TimeService {

    public LocalDate getToday(){
        return LocalDate.now();
    }
}
