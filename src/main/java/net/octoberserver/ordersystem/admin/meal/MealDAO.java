package net.octoberserver.ordersystem.admin.meal;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.octoberserver.ordersystem.meal.MealOptionDTO;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
class GetMealDetailedResponseDAO {
    private List<MealOptionDTO> options;
}


@Data
@AllArgsConstructor
class PatchMealDetailedRequest {
    private LocalDate date;
    private List<MealOptionDTO> options;
}