package garage.reservation.fastandfurious.service.exception;

import java.time.LocalDate;

public class OperatingHoursNotFoundException extends RuntimeException {
    public OperatingHoursNotFoundException(LocalDate date) {
        super("No operating hours found for given date: " + date.toString());
    }
}
