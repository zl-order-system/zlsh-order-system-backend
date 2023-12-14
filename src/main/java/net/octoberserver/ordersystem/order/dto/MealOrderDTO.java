package net.octoberserver.ordersystem.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.octoberserver.ordersystem.meal.Meal;
import net.octoberserver.ordersystem.order.LunchBox;
import net.octoberserver.ordersystem.order.OrderData;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
public class MealOrderDTO {
    private OrderData orderData;
    private Meal meal;
}
