package net.octoberserver.ordersystem.admin.payments.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class GetPaymentDataResponseDAO {
    private boolean locked;
    private List<Item> data;

    @Data
    @Builder
    @AllArgsConstructor
    public static class Item {
        private long userID;
        private String name;
        private short seatNumber;
        private String lunchBoxType;
        private String mealName;
        private boolean paid;
    }
}