package net.octoberserver.ordersystem.order;

import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.meal.Meal;
import net.octoberserver.ordersystem.order.dao.GetOrderDataDAO;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public GetOrderDataDAO getOrderData(long userID) {
        return processOrderData(orderRepository.findUpcomingMealsWithOrders(userID));
    }

    private GetOrderDataDAO processOrderData(@NotNull List<Tuple> mealOrders) {
        final var headerData = new GetOrderDataDAO.HeaderData(0, 0, 0);
        final var daoOrderItems = new ArrayList<GetOrderDataDAO.DaoOrderItem>();

        mealOrders.forEach(mealOrder -> {
            final Meal meal = mealOrder.get(0, Meal.class);
            final OrderData orderData = mealOrder.get(1, OrderData.class);
            final LocalDate date = meal.getDate();
            final String displayDate = date.toString();
            final List<String> mealOptions = meal.getOptions();

            if (orderData == null) {
                daoOrderItems.add(GetOrderDataDAO.DaoOrderItem
                    .builder()
                    .date(date)
                    .displayDate(displayDate)
                    .mealOptions(mealOptions)
                    .state(GetOrderDataDAO.OrderState.UNORDERED)
                    .price("-")
                    .lunchBox("-")
                    .selectedMeal("-")
                    .build()
                );
                headerData.setDaysUnordered(headerData.getDaysUnordered() + 1);
                return;
            }

            final int price = getPrice(orderData.lunchBox);

            final String orderState;
            if (orderData.paid) {
                orderState = GetOrderDataDAO.OrderState.PAID;
                headerData.setPaid(headerData.getPaid() + price);
            } else {
                orderState = GetOrderDataDAO.OrderState.UNPAID;
                headerData.setOwed(headerData.getOwed() + price);
            }

            daoOrderItems.add(GetOrderDataDAO.DaoOrderItem
                .builder()
                .date(date)
                .displayDate(displayDate)
                .mealOptions(mealOptions)
                .state(orderState)
                .price(Integer.toString(price))
                .lunchBox(getLunchBoxString(orderData.lunchBox))
                .selectedMeal(Short.toString(orderData.meal))
                .build()
            );
        });
        return new GetOrderDataDAO(headerData, daoOrderItems);
    }

    public int getPrice(@NotNull LunchBox lunchBox) {
        return switch (lunchBox) {
            case PERSONAL -> 65;
            case SCHOOL -> 70;
        };
    }

    public String getLunchBoxString(@NotNull LunchBox lunchBox) {
        return switch (lunchBox) {
            case PERSONAL -> GetOrderDataDAO.LunchBoxType.PERSONAL;
            case SCHOOL -> GetOrderDataDAO.LunchBoxType.SCHOOL;
        };
    }
}
