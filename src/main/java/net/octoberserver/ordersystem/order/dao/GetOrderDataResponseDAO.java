package net.octoberserver.ordersystem.order.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import net.octoberserver.ordersystem.meal.MealOption;

import java.util.List;
import java.util.UUID;

public record GetOrderDataResponseDAO(
    HeaderData headerData,
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
        String date,
        String lunchBox,
        Integer price,
        Short selectedMeal,
        List<MealOption> mealOptions,
        boolean locked
    ) {}
}
