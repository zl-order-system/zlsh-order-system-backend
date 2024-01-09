package net.octoberserver.ordersystem.order;

import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.meal.Meal;
import net.octoberserver.ordersystem.order.dao.GetOrderDataDAO;
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

    private GetOrderDataDAO processOrderData(List<Tuple> mealOrders) {
        var headerData = new GetOrderDataDAO.HeaderData(0, 0, 0);
        var daoOrderItems = new ArrayList<GetOrderDataDAO.DaoOrderItem>();

        mealOrders.forEach(mealOrder -> {
            Meal meal = mealOrder.get(0, Meal.class);
            OrderData orderData = mealOrder.get(1, OrderData.class);
            LocalDate date = meal.getDate();
            String displayDate = date.toString();
            List<String> mealOptions = meal.getOptions();

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

            int price = getPrice(orderData.lunchBox);

            String orderState;
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

    public int getPrice(LunchBox lunchBox) {
        return switch (lunchBox) {
            case PERSONAL -> 65;
            case SCHOOL -> 70;
        };
    }

    public String getLunchBoxString(LunchBox lunchBox) {
        return switch (lunchBox) {
            case PERSONAL -> GetOrderDataDAO.LunchBoxType.PERSONAL;
            case SCHOOL -> GetOrderDataDAO.LunchBoxType.SCHOOL;
        };
    }
}
