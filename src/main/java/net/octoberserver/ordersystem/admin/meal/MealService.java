package net.octoberserver.ordersystem.admin.meal;

import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.meal.MealRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MealService {

    private final MealRepository mealRepository;

    GetMealDetailedResponseDAO getMealDetailed(LocalDate date) {
        final var meal = mealRepository.findById(date);
        if (meal.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot find data for that date");
        return new GetMealDetailedResponseDAO(meal.get().getOptions());
    }

    void patchMealDetailed(PatchMealDetailedRequest request) {
        final var mealOptional = mealRepository.findById(request.getDate());
        if (mealOptional.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Meal not found");

        final var meal = mealOptional.get();
        meal.setOptions(request.getOptions());
        try {
            mealRepository.save(meal);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Options");
        }
    }
}
