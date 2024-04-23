package garage.reservation.fastandfurious.resource.dto;

import garage.reservation.fastandfurious.domain.OperatingHours;
import lombok.Builder;
import lombok.Value;
import org.springframework.lang.Nullable;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    Boolean isOn;

    public static OperatingHoursResponse from(OperatingHours operatingHours) {
        if (operatingHours.getDayOfWeek() == null) {
            return fromSpecificDate(operatingHours);
        } else {
            return OperatingHoursResponse.builder()
                    .id(operatingHours.getId())
                    .openingTime(operatingHours.getOpenTime())
                    .closingTime(operatingHours.getCloseTime())
                    .dayOfWeek(operatingHours.getDayOfWeek().name())
                    .specificDate(operatingHours.getSpecificDate())
                    .isOn(!operatingHours.isOffDay())
                    .build();
        }
    }

    public static OperatingHoursResponse fromSpecificDate(OperatingHours operatingHours) {
        return OperatingHoursResponse.builder()
                .id(operatingHours.getId())
                .openingTime(operatingHours.getOpenTime())
                .closingTime(operatingHours.getCloseTime())
                .specificDate(operatingHours.getSpecificDate())
                .isOn(!operatingHours.isOffDay())
                .build();
    }
}
