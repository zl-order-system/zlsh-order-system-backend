package net.octoberserver.ordersystem.order.dao;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateOrderDataRequestDAO(
    @NotNull
    UUID id,
    @NotBlank
    String lunchBoxType,
    @Min(0)
    short selectedMeal
) {}

