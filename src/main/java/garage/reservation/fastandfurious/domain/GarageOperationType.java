package garage.reservation.fastandfurious.domain;

import lombok.Getter;

@Getter
public enum GarageOperationType {
    GENERAL_CHECK("General Check", 3.0),
    TIRE_REPLACEMENT("Tire Replacement", 1.0),
    BROKEN_LAMP_CHANGE("Broken Lamp Change", 0.5);

    private final String displayName;
    private final double durationHours;

    GarageOperationType(String displayName, double durationHours) {
        this.displayName = displayName;
        this.durationHours = durationHours;
    }
}
