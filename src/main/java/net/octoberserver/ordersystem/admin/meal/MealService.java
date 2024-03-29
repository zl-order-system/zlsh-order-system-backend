package net.octoberserver.ordersystem.admin.meal;

import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.admin.meal.dao.GetMealDetailedResponseDAO;
import net.octoberserver.ordersystem.admin.meal.dao.UpdateMealDetailedRequestDAO;
import net.octoberserver.ordersystem.meal.Meal;
import net.octoberserver.ordersystem.meal.MealRepository;
import net.octoberserver.ordersystem.order.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MealService {

    private final MealRepository mealRepository;
    private final OrderRepository orderRepository;

    GetMealDetailedResponseDAO getMealDetailed(LocalDate date) {
        final var mutable = orderRepository.findFirstByDate(date).isEmpty();
        final var options = mealRepository
            .findById(date)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot find data for that date"))
            .getOptions();
        return new GetMealDetailedResponseDAO(mutable, options);
    }

    void updateMealDetailed(UpdateMealDetailedRequestDAO request) {
        if (orderRepository.findFirstByDate(request.date()).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Date has been ordered");

        if (request.options().isEmpty()) {
            mealRepository.deleteById(request.date());
            return;
        }

        try {
            mealRepository.save(new Meal(request.date(), request.options()));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid options");
        }
    }
}
