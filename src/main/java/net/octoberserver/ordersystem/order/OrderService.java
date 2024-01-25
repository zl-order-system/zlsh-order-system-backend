package net.octoberserver.ordersystem.order;

import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.common.LunchBoxService;
import net.octoberserver.ordersystem.meal.Meal;
import net.octoberserver.ordersystem.meal.MealOption;
import net.octoberserver.ordersystem.order.dao.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final LunchBoxService lunchBoxService;

    public GetHomeDataDAO processHomeData(List<Tuple> mealOrders, LocalDate today) {
        final var headerData = new GetHomeDataDAO.BannerData(today, false, 0, 0);
        mealOrders.forEach(mealOrder -> {
            final OrderData orderData = mealOrder.get(1, OrderData.class);
            if (orderData == null) {
                return;
            }
            if (!orderData.isPaid()) {
                headerData.setOwed(headerData.getOwed() + lunchBoxService.getPrice(orderData.getLunchBox()));
            }
            if (orderData.getDate().equals(today)) {
                headerData.setHasPaidToday(orderData.isPaid());
            }
            headerData.setDaysOrdered(headerData.getDaysOrdered() + 1);
        });

        var bodyData = mealOrders
            .stream()
            .map(mealOrder -> {
                var orderData = mealOrder.get(1, OrderData.class);
                var meal = mealOrder.get(1, Meal.class);
                if (orderData == null) {
                    return new GetHomeDataDAO.PreviewData(meal.getDate(), false);
                }
                return new GetHomeDataDAO.PreviewData(orderData.getDate(), true);
            })
            .collect(Collectors.toList());
        return new GetHomeDataDAO(headerData, bodyData);
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
                .selectedMeal(Short.toString(orderData.getMeal()))
                .build()
            );
        });
        return new GetOrderDataDAO(headerData, daoOrderItems);
    }
}
