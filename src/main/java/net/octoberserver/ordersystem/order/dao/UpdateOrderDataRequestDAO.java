package net.octoberserver.ordersystem.order.dao;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UpdateOrderDataRequestDAO(
    @FutureOrPresent
    @NotNull
    LocalDate date,
    @NotBlank
    String lunchBoxType,
    @Min(0)
    short selectedMeal
) {}

