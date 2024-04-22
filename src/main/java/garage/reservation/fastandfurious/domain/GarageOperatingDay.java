package garage.reservation.fastandfurious.domain;

import lombok.Builder;
import lombok.Value;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Value
@Builder
public class GarageOperatingDay {
    LocalDate date;
    LocalTime openingTime;
    LocalTime closingTime;
    List<Appointment> appointments;
}
