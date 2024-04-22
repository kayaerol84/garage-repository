package garage.reservation.fastandfurious.service.exception;

public class MechanicNotFoundException extends  RuntimeException{
    public MechanicNotFoundException(Long id) {
        super("Mechanic not found for given Id: " + id);
    }
}
