package garage.reservation.fastandfurious.resource.dto;

import garage.reservation.fastandfurious.domain.OperatingHours;
import lombok.Builder;
import lombok.Value;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Value
@Builder
public class SaveOperatingHoursRequest {
    Long id;
    String dayOfWeek;
    LocalDate specificDate;
    LocalTime openTime;
    LocalTime closeTime;

    public OperatingHours toOperatingHours(Long id){
        return OperatingHours.builder()
                .openTime(this.openTime)
                .closeTime(this.closeTime)
                .id(id)
                .dayOfWeek(DayOfWeek.valueOf(this.dayOfWeek))
                .specificDate(this.specificDate)
                .build();
    }

    public OperatingHours toSpecificDateOperatingHours(){
        return OperatingHours.builder()
                .openTime(this.openTime)
                .closeTime(this.closeTime)
                .specificDate(this.specificDate)
                .build();
    }
}
