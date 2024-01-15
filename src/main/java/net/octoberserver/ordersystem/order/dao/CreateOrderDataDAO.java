package net.octoberserver.ordersystem.order.dao;

import java.time.LocalDate;
import java.util.UUID;

public class CreateOrderDataDAO {
    public record Request(LocalDate date, String lunchBoxType, short selectedMeal) {}
    public record Response(UUID id) {}
}
