package net.octoberserver.ordersystem.admin.payments.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class GetPaymentDataResponseDAO {
    private long userID;
    private String name;
    private short seatNumber;
    private String lunchBoxType;
    private String mealName;
    private boolean paid;
}