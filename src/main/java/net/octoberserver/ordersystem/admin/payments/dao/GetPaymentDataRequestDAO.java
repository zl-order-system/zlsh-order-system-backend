package net.octoberserver.ordersystem.admin.payments.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class GetPaymentDataRequestDAO extends GetPaymentDataResponseDAO {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        private LocalDate date;
    }
}
