package net.octoberserver.ordersystem.admin.meal.dao;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import net.octoberserver.ordersystem.meal.MealOption;

import java.time.LocalDate;
import java.util.List;

public record UpdateMealDetailedRequestDAO(
    @FutureOrPresent
    @NotNull
    LocalDate date,
    @Valid
    @NotNull
    List<MealOption> options
) {}