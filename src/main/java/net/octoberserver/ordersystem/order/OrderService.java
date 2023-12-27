package net.octoberserver.ordersystem.order;

import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.meal.Meal;
import net.octoberserver.ordersystem.order.dao.GetOrderDataDAO;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public GetOrderDataDAO getOrderData(long userID) {
        List<GetOrderDataDAO.BodyData> bodyData = new ArrayList<>();

        var headerData = new GetOrderDataDAO.HeaderData(0, 0, 0);

        List<Tuple> mealOrders = orderRepository.findUpcomingMealsWithOrders(userID);

        mealOrders.forEach(mealOrder -> {
            Meal meal = mealOrder.get(0, Meal.class);
            OrderData orderData = mealOrder.get(1, OrderData.class);
            LocalDate date = meal.getDate();
            String displayDate = date.toString();
            List<String> mealOptions = meal.getOptions();

            if (orderData == null) {
                bodyData.add(GetOrderDataDAO.BodyData
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

            bodyData.add(GetOrderDataDAO.BodyData
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
        return new GetOrderDataDAO(headerData, bodyData);
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
