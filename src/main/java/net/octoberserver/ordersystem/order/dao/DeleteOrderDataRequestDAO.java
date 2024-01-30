package net.octoberserver.ordersystem.order.dao;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DeleteOrderDataRequestDAO(
    @NotNull
    UUID id
) {}
