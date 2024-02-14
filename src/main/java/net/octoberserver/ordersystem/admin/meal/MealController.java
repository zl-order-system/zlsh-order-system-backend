package net.octoberserver.ordersystem.admin.meal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.admin.meal.dao.GetMealDetailedResponseDAO;
import net.octoberserver.ordersystem.admin.meal.dao.UpdateMealDetailedRequestDAO;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/admin/meal")
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;

    @GetMapping("/detailed")
    GetMealDetailedResponseDAO getMealDetailed(@RequestParam(name = "date") @FutureOrPresent LocalDate date) {
        return mealService.getMealDetailed(date);
    }

    @PutMapping("/detailed")
    void updateMealDetailed(@RequestBody @Valid UpdateMealDetailedRequestDAO request) {
        mealService.updateMealDetailed(request);
    }
}
