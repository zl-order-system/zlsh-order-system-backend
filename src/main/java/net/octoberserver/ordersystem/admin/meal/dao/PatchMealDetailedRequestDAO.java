package net.octoberserver.ordersystem.admin.meal.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.octoberserver.ordersystem.meal.MealOption;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatchMealDetailedRequestDAO {
    private LocalDate date;
    private List<MealOption> options;
}