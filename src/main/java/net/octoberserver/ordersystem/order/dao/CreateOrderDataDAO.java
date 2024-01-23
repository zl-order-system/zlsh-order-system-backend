package net.octoberserver.ordersystem.order.dao;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public class CreateOrderDataDAO {
    public record Request(
        @NotNull
        LocalDate date,
        @NotBlank
        String lunchBoxType,
        @Min(0)
        short selectedMeal
    ) {}
    public record Response(
        UUID id
    ) {}
}
