package net.octoberserver.ordersystem.meal;


import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.octoberserver.ordersystem.util.ListToJsonConverter;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Meal {
    @Id
    private LocalDate date;
    @Convert(converter = ListToJsonConverter.class)
    private List<MealOptionDTO> options;
}
