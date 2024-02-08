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

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final LunchBoxService lunchBoxService;
    private final MealRepository mealRepository;
    private final OrderRepository orderRepository;

    public GetOrderDataResponseDAO getOrderData(long userID) {
        return processOrderData(orderRepository.findUpcomingMealsWithOrders(userID));
    }

    public CreateOrderDataResponseDAO createOrderData(CreateOrderDataRequestDAO request, long userID) {
        final var order = orderRepository.findByDateAndUserID(request.date(), userID);
        if (order.isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OrderData for that day already exist");
        final var id = UUID.randomUUID();
        orderRepository.save(OrderData.builder()
            .ID(id)
            .date(request.date())
            .lunchBox(lunchBoxService.getLunchBoxEnum(request.lunchBoxType()))
            .userID(userID)
            .mealOption(request.selectedMeal())
            .paid(false)
            .build()
        );
        return new CreateOrderDataResponseDAO(id);
    }

    public void updateOrderData(UpdateOrderDataRequestDAO request, long userID) {
        final var orderData = orderRepository
            .findByDateAndUserID(request.date(), userID)
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

    public void deleteOrderData(DeleteOrderDataRequestDAO request) {
        try {
            orderRepository.deleteById(request.id());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public GetOrderDataResponseDAO processOrderData(List<Tuple> mealOrders) {
        final var headerData = new GetOrderDataResponseDAO.HeaderData(0, 0, 0);
        final var daoOrderItems = new ArrayList<GetOrderDataResponseDAO.DaoOrderItem>();

        mealOrders.forEach(mealOrder -> {
            final Meal meal = mealOrder.get(0, Meal.class);
            final OrderData orderData = mealOrder.get(1, OrderData.class);
            final LocalDate date = meal.getDate();
            final String displayDate = date.format(DateTimeFormatter.ofPattern("M/d E", new Locale("zh", "TW")));
            final List<MealOption> mealOptions = meal.getOptions();

            if (orderData == null) {
                daoOrderItems.add(GetOrderDataResponseDAO.DaoOrderItem
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

            daoOrderItems.add(GetOrderDataResponseDAO.DaoOrderItem
                .builder()
                .date(date)
                .displayDate(displayDate)
                .id(orderData.getID())
                .mealOptions(mealOptions)
                .state(orderState)
                .price(Integer.toString(price))
                .lunchBox(lunchBoxService.getLunchBoxString(orderData.getLunchBox()))
                .selectedMeal(meal.getOptions().get(orderData.getMealOption()).getName())
                .build()
            );
        });
        return new GetOrderDataResponseDAO(headerData, daoOrderItems);
    }
}
