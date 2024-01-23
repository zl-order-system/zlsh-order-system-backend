package net.octoberserver.ordersystem.admin.payments.dao;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

public class GetPaymentDataDAO {
    @Data
    @AllArgsConstructor
    public static class Request {
        private LocalDate date;
    }

    @Data
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
