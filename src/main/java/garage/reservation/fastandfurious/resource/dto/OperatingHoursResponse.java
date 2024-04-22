package garage.reservation.fastandfurious.resource.dto;

import garage.reservation.fastandfurious.domain.OperatingHours;
import lombok.Builder;
import lombok.Value;
import java.time.LocalDate;
import java.time.LocalTime;

@Value
@Builder
public class OperatingHoursResponse {
    Long id;
    LocalTime openingTime;
    LocalTime closingTime;
    String dayOfWeek;
    LocalDate specificDate;

    public static OperatingHoursResponse from(OperatingHours operatingHours) {
        return OperatingHoursResponse.builder()
                .id(operatingHours.getId())
                .openingTime(operatingHours.getOpenTime())
                .closingTime(operatingHours.getCloseTime())
                .dayOfWeek(operatingHours.getDayOfWeek().name())
                .specificDate(operatingHours.getSpecificDate())
                .build();
    }
}
