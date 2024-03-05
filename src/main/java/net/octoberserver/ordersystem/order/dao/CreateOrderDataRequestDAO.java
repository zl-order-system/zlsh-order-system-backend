package net.octoberserver.ordersystem.order.dao;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CreateOrderDataRequestDAO(
    @FutureOrPresent
    @NotNull
    LocalDate date,
    @NotBlank
    String lunchBoxType,
    @Min(0)
    Short selectedMeal
) {}