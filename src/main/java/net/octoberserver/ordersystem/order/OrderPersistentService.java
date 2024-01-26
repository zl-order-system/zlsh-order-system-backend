package net.octoberserver.ordersystem.order;

import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.common.LunchBoxService;
import net.octoberserver.ordersystem.meal.MealRepository;
import net.octoberserver.ordersystem.order.dao.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class OrderPersistentService {

    private final OrderService orderService;
    private final LunchBoxService lunchBoxService;
    private final OrderRepository orderRepository;
    private final MealRepository mealRepository;

    public GetHomeDataDAO getHomeData(long userID, LocalDate today) {
        return orderService.processHomeData(orderRepository.findUpcomingMealsWithOrders(userID), today);
    }

    public GetOrderDataDAO getOrderData(long userID) {
        return orderService.processOrderData(orderRepository.findUpcomingMealsWithOrders(userID));
    }

    public CreateOrderDataResponseDAO createOrderData(CreateOrderDataRequestDAO request, long userID) {
        return new CreateOrderDataResponseDAO(orderRepository.save(OrderData.builder()
            .date(request.date())
            .lunchBox(lunchBoxService.getLunchBoxEnum(request.lunchBoxType()))
            .userID(userID)
            .meal(request.selectedMeal())
            .paid(false)
            .build()
        ).getID());
    }

    public void updateOrderData(UpdateOrderDataRequestDAO request) {
        final var orderData = orderRepository
            .findById(request.id())
            .orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "OrderData not found"));

        final var meal = mealRepository
            .findById(orderData.getDate())
            .orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Meal not found"));

        if (request.selectedMeal() >= meal.getOptions().size())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Option not available");

        orderData.setMeal(request.selectedMeal());
        orderData.setLunchBox(lunchBoxService.getLunchBoxEnum(request.lunchBoxType()));
        orderRepository.save(orderData);
    }

    public void deleteOrderData(DeleteOrderDataDAO request) {
        try {
            orderRepository.deleteById(request.id());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

}
