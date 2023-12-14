package net.octoberserver.ordersystem.order;

import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.meal.Meal;
import net.octoberserver.ordersystem.order.dao.GetOrderDataDAO;
import net.octoberserver.ordersystem.order.dto.MealOrderDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Optional<GetOrderDataDAO> getOrderData() {
        List<GetOrderDataDAO.BodyData> bodyData = new ArrayList<>();

        // TODO: Use a loop to append data to list

        var headerData = new GetOrderDataDAO.HeaderData(0, 0, 0);

        List<MealOrderDTO> mealOrders = orderRepository.findUpcomingMealsWithOrders();
        mealOrders.forEach(mealOrder -> {
            Meal meal = mealOrder.getMeal();
            OrderData orderData = mealOrder.getOrderData();
            LocalDate date = meal.getDate();
            String displayDate = date.toString();
            String mealOptions = meal.getOptionsAsString();

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
                .selectedMeal(meal.getOptions().get(orderData.meal))
                .build()
            );

            System.out.println(mealOrder.getMeal().toString());
            System.out.println(mealOrder.getOrderData().toString());
        });

        return Optional.of(new GetOrderDataDAO(headerData, bodyData));
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
