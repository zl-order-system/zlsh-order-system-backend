package net.octoberserver.ordersystem.admin.meal;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/admin/meal")
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;

    @GetMapping("/detailed")
    GetMealDetailedResponseDAO getMealDetailed(@RequestParam(name = "date") LocalDate date) {
        return mealService.getMealDetailed(date);
    }

    @PatchMapping
    void patchMealDetailed(@RequestBody PatchMealDetailedRequest request) {
        mealService.patchMealDetailed(request);
    }
}
