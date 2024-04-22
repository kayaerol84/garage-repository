package garage.reservation.fastandfurious.resource.exception;

public class InvalidOffDayException extends RuntimeException{
    public InvalidOffDayException() {
        super("Off day should be one of the following values: [MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY]");
    }
}
