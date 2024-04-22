package garage.reservation.fastandfurious.service.exception;

public class InvalidAppointmentDateTimeException extends RuntimeException {
    public InvalidAppointmentDateTimeException(String message) {
        super(message);
    }
}
