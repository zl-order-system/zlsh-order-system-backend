package net.octoberserver.ordersystem.admin.meal.dao;

import net.octoberserver.ordersystem.meal.MealOption;

import java.util.List;

public record GetMealDetailedResponseDAO(
    boolean mutable,
    List<MealOption> options
) {}
