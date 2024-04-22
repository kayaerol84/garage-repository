package garage.reservation.fastandfurious.resource;

import garage.reservation.fastandfurious.resource.exception.InvalidOffDayException;
import garage.reservation.fastandfurious.service.exception.InvalidAppointmentDateTimeException;
import garage.reservation.fastandfurious.service.exception.MechanicNotAvailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

// TODO refactor handling Bad Requests
@RestControllerAdvice
public class ReservationExceptionHandler {

    @ExceptionHandler({MechanicNotAvailableException.class})
    public ResponseEntity<String> handleMechanicNotAvailableException(MechanicNotAvailableException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidOffDayException.class})
    public ResponseEntity<String> handleInvalidOffDayException(InvalidOffDayException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidAppointmentDateTimeException.class})
    public ResponseEntity<String> handleInvalidAppointmentDateTimeException(InvalidAppointmentDateTimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // TODO handle all the rest
    @ExceptionHandler({Exception.class})
    public ResponseEntity<String> handleAllExceptions(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
