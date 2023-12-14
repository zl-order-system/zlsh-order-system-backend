package net.octoberserver.ordersystem.order.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class GetOrderDataDAO {
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
    public static class BodyData {
        private String state;
        private LocalDate date;
        private String displayDate;
        private String lunchBox;
        private String price;
        private String selectedMeal;
        private String mealOptions;
    }

    public static class OrderState {
        public static final String PAID = "已繳費";
        public static final String UNPAID = "未繳費";
        public static final String UNORDERED = "未訂餐";
    }

    public static class LunchBoxType {
        public static final String SCHOOL = "學校餐盒";
        public static final String PERSONAL = "自備餐盒";
    }

    private HeaderData headerData;
    private List<BodyData> bodyData;
}
