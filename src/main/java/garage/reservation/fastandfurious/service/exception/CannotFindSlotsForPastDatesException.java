package garage.reservation.fastandfurious.service.exception;

public class CannotFindSlotsForPastDatesException extends RuntimeException {
    public CannotFindSlotsForPastDatesException() {
        super("No slots can be found for the past dates");
    }
}
