package net.octoberserver.ordersystem.order;

import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.meal.Meal;
import net.octoberserver.ordersystem.meal.MealOption;
import net.octoberserver.ordersystem.meal.MealRepository;
import net.octoberserver.ordersystem.order.dao.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final LunchBoxService lunchBoxService;
    private final MealRepository mealRepository;
    private final OrderRepository orderRepository;

    public GetOrderDataDAO getOrderData(long userID) {
        return processOrderData(orderRepository.findUpcomingMealsWithOrders(userID));
    }

    public CreateOrderDataResponseDAO createOrderData(CreateOrderDataRequestDAO request, long userID) {
        return new CreateOrderDataResponseDAO(orderRepository.save(OrderData.builder()
            .date(request.date())
            .lunchBox(lunchBoxService.getLunchBoxEnum(request.lunchBoxType()))
            .userID(userID)
            .mealOption(request.selectedMeal())
            .paid(false)
            .build()
        ).getID());
    }

    public void updateOrderData(UpdateOrderDataRequestDAO request) {
        final var orderData = orderRepository
            .findById(request.id())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "OrderData not found"));

        final var meal = mealRepository
            .findById(orderData.getDate())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Meal not found"));

        if (request.selectedMeal() >= meal.getOptions().size())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Option not available");

        orderData.setMealOption(request.selectedMeal());
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

    public GetOrderDataDAO processOrderData(List<Tuple> mealOrders) {
        final var headerData = new GetOrderDataDAO.HeaderData(0, 0, 0);
        final var daoOrderItems = new ArrayList<GetOrderDataDAO.DaoOrderItem>();

        mealOrders.forEach(mealOrder -> {
            final Meal meal = mealOrder.get(0, Meal.class);
            final OrderData orderData = mealOrder.get(1, OrderData.class);
            final LocalDate date = meal.getDate();
            final String displayDate = date.toString();
            final List<MealOption> mealOptions = meal.getOptions();

            if (orderData == null) {
                daoOrderItems.add(GetOrderDataDAO.DaoOrderItem
                    .builder()
                    .date(date)
                    .displayDate(displayDate)
                    .id(null)
                    .mealOptions(mealOptions)
                    .state(OrderState.UNORDERED)
                    .price("-")
                    .lunchBox("-")
                    .selectedMeal("-")
                    .build()
                );
                headerData.setDaysUnordered(headerData.getDaysUnordered() + 1);
                return;
            }

            final int price = lunchBoxService.getPrice(orderData.getLunchBox());

            final String orderState;
            if (orderData.isPaid()) {
                orderState = OrderState.PAID;
                headerData.setPaid(headerData.getPaid() + price);
            } else {
                orderState = OrderState.UNPAID;
                headerData.setOwed(headerData.getOwed() + price);
            }

            daoOrderItems.add(GetOrderDataDAO.DaoOrderItem
                .builder()
                .date(date)
                .displayDate(displayDate)
                .id(orderData.getID())
                .mealOptions(mealOptions)
                .state(orderState)
                .price(Integer.toString(price))
                .lunchBox(lunchBoxService.getLunchBoxString(orderData.getLunchBox()))
                .selectedMeal(Short.toString(orderData.getMealOption()))
                .build()
            );
        });
        return new GetOrderDataDAO(headerData, daoOrderItems);
    }
}
