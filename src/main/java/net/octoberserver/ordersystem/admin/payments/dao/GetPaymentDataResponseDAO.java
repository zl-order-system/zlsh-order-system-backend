package net.octoberserver.ordersystem.admin.payments.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
public record GetPaymentDataResponseDAO(
    boolean locked,
    List<Item> data
) {
    @Builder
    public record Item(
        long userID,
        String name,
        short seatNumber,
        String lunchBoxType,
        String mealName,
        boolean paid
    ) {}
}