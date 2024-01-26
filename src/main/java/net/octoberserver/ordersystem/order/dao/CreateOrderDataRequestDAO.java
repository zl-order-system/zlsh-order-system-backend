package net.octoberserver.ordersystem.order.dao;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateOrderDataRequestDAO(
    @NotNull
    LocalDate date,
    @NotBlank
    String lunchBoxType,
    @Min(0)
    short selectedMeal
) {
}