package net.octoberserver.ordersystem.order.dao;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record DeleteOrderDataRequestDAO(
    @NotNull
    LocalDate date
) {}
