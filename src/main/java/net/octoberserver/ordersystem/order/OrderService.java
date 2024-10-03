package net.octoberserver.ordersystem.order;

import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.meal.*;
import net.octoberserver.ordersystem.order.dao.*;
import net.octoberserver.ordersystem.order.lunchbox.LunchBoxService;
import net.octoberserver.ordersystem.websocket.WebSocketService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static net.octoberserver.ordersystem.Utils.formatApiDate;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final LunchBoxService lunchBoxService;
    private final MealRepository mealRepository;
    private final OrderRepository orderRepository;
    private final MealClassLockRepository classLockRepository;
    private final WebSocketService webSocketService;

    public GetOrderDataResponseDAO getOrderData(long userID, short classNumber) {
        return processOrderData(orderRepository.findUpcomingMealsWithOrders(userID), classNumber);
    }

    public CreateOrderDataResponseDAO createOrderData(CreateOrderDataRequestDAO request, long userID, short classNumber) {
        final var locked = classLockRepository.findById(MealClassLock.generateID(request.date(), classNumber)).isPresent();
        if (locked)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The date has been locked by an admin");

        mealRepository.findById(request.date()).ifPresent(meal -> {
            if (request.selectedMeal() > meal.getOptions().size() - 1)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid meal option: " + request.selectedMeal());
        });

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


        webSocketService.sendOrderDataToClient(userID, classNumber, this);
        return new CreateOrderDataResponseDAO(id);
    }

    public void updateOrderData(UpdateOrderDataRequestDAO request, long userID, short classNumber) {
        final var orderData = orderRepository
            .findByDateAndUserID(request.date(), userID)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "OrderData not found"));

        final var meal = mealRepository
            .findById(orderData.getDate())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Meal not found"));

        final var locked = classLockRepository.findById(MealClassLock.generateID(request.date(), classNumber)).isPresent();
        if (locked)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The date has been locked by an admin");

        if (orderData.isPaid())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot edit OrderData that has been paid already");

        if (request.selectedMeal() >= meal.getOptions().size())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Option not available");

        orderData.setMealOption(request.selectedMeal());
        orderData.setLunchBox(lunchBoxService.getLunchBoxEnum(request.lunchBoxType()));
        orderRepository.save(orderData);

        webSocketService.sendOrderDataToClient(userID, classNumber, this);
    }

    public void deleteOrderData(DeleteOrderDataRequestDAO request, long userID, short classNumber) {
        final var locked = classLockRepository.findById(MealClassLock.generateID(request.date(), classNumber)).isPresent();

        if (locked)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The date has been locked by an admin");

        final var orderData = orderRepository.findByDateAndUserID(request.date(), userID)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot find order data"));

        if (orderData.isPaid())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot delete OrderData that has been paid already");

        orderRepository.delete(orderData);

        webSocketService.sendOrderDataToClient(userID, classNumber, this);
    }

    public GetOrderDataResponseDAO processOrderData(List<Tuple> mealOrders, short classNumber) {
        final var headerData = new GetOrderDataResponseDAO.HeaderData(0, 0, 0);
        final var daoOrderItems = new ArrayList<GetOrderDataResponseDAO.DaoOrderItem>();

        mealOrders.forEach(mealOrder -> {
            final Meal meal = mealOrder.get(0, Meal.class);
            final OrderData orderData = mealOrder.get(1, OrderData.class);
            final var locked = classLockRepository.findById(MealClassLock.generateID(meal.getDate(), classNumber)).isPresent();

            // Remove later
            if (locked) return;

            final LocalDate date = meal.getDate();
            final List<MealOption> mealOptions = meal.getOptions();

            if (orderData == null) {
                daoOrderItems.add(GetOrderDataResponseDAO.DaoOrderItem
                    .builder()
                    .date(formatApiDate(date))
                    .id(null)
                    .mealOptions(mealOptions)
                    .state(OrderState.UNORDERED)
                    .price(null)
                    .lunchBox(null)
                    .selectedMeal(null)
                    .locked(locked)
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
                .date(formatApiDate(date))
                .id(orderData.getID())
                .mealOptions(mealOptions)
                .state(orderState)
                .price(price)
                .lunchBox(lunchBoxService.getLunchBoxString(orderData.getLunchBox()))
                .selectedMeal(orderData.getMealOption())
                .locked(locked)
                .build()
            );
        });
        return new GetOrderDataResponseDAO(headerData, daoOrderItems);
    }
}
