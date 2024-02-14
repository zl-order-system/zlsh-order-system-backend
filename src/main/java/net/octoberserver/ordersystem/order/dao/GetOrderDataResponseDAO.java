package net.octoberserver.ordersystem.order.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import net.octoberserver.ordersystem.meal.MealOption;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class GetOrderDataResponseDAO {
    @Data
    @AllArgsConstructor
    public static class HeaderData {
        private int paid;
        private int owed;
        private int daysUnordered;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class DaoOrderItem {
        private String state;
        private LocalDate date;
        private String displayDate;
        private UUID id;
        private String lunchBox;
        private String price;
        private String selectedMeal;
        private List<MealOption> mealOptions;
        private boolean locked;
    }

    private HeaderData headerData;
    private List<DaoOrderItem> bodyData;
}
