package net.octoberserver.ordersystem.admin.payments.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class GetPaymentDataDAO {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        private LocalDate date;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class Response {
        private long id;
        private String name;
        private short seatNumber;
        private String lunchBoxType;
        private String mealName;
        private boolean paid;
    }
}
