package net.octoberserver.ordersystem.admin.meal;

import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.admin.meal.dao.GetMealDetailedResponseDAO;
import net.octoberserver.ordersystem.admin.meal.dao.PatchMealDetailedRequestDAO;
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
        return new GetMealDetailedResponseDAO(
            mealRepository
                .findById(date)
                .orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot find data for that date"))
                .getOptions());
    }

    void patchMealDetailed(PatchMealDetailedRequestDAO request) {
        final var meal = mealRepository
            .findById(request.getDate())
            .orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Meal not found"));
        meal.setOptions(request.getOptions());
        try {
            mealRepository.save(meal);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Options");
        }
    }
}
