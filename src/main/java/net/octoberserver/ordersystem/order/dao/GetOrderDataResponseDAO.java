package net.octoberserver.ordersystem.order.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import net.octoberserver.ordersystem.meal.MealOption;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record GetOrderDataResponseDAO(
    net.octoberserver.ordersystem.order.dao.GetOrderDataResponseDAO.HeaderData headerData,
    List<DaoOrderItem> bodyData
) {
    @AllArgsConstructor
    @Data
    public static class HeaderData {
        private int paid;
        private int owed;
        private int daysUnordered;
    }

    @Builder
    public record DaoOrderItem(
        UUID id,
        String state,
        LocalDate date,
        String displayDate,
        String lunchBox,
        String price,
        String selectedMeal,
        List<MealOption> mealOptions,
        boolean locked
    ) {}
}
