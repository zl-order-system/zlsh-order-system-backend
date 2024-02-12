package net.octoberserver.ordersystem.admin.meal.dao;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.octoberserver.ordersystem.meal.MealOption;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetMealDetailedResponseDAO {
    private boolean mutable;
    private List<MealOption> options;
}
