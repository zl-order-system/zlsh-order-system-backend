package net.octoberserver.ordersystem.order.dao;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DeleteOrderDataDAO(
    @NotNull
    UUID id
) {}
