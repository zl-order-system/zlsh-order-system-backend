package net.octoberserver.ordersystem.meal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class MealClassLock {
    @Id
    private String id;
    private LocalDate mealID;
    private short classNumber;

    public static String generateID(LocalDate date, short classNumber) {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "_" + classNumber;
    }
}
