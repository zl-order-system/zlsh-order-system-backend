package net.octoberserver.ordersystem.admin.meal.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.octoberserver.ordersystem.meal.MealOptionDTO;

import java.util.List;

@Data
@AllArgsConstructor
public class GetMealDetailedResponseDAO {
    private List<MealOptionDTO> options;
}
