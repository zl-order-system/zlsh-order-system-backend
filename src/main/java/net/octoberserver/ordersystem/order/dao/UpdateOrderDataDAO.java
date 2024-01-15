package net.octoberserver.ordersystem.order.dao;

import java.util.UUID;

public record UpdateOrderDataDAO(UUID id, String lunchBoxType, short selectedMeal) {}

