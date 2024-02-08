package net.octoberserver.ordersystem.admin.meal.dao;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.octoberserver.ordersystem.meal.MealOption;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMealDetailedRequestDAO {
    @FutureOrPresent
    @NotNull
    private LocalDate date;
    @Valid
    @NotNull
    private List<MealOption> options;
}