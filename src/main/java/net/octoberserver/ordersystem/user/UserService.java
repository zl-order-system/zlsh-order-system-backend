package net.octoberserver.ordersystem.user;

import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.meal.Meal;
import net.octoberserver.ordersystem.order.LunchBoxService;
import net.octoberserver.ordersystem.order.OrderData;
import net.octoberserver.ordersystem.order.dao.GetHomeDataDAO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

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
                var meal = mealOrder.get(0, Meal.class);
                var orderData = mealOrder.get(1, OrderData.class);
                if (orderData == null) {
                    return new GetHomeDataDAO.PreviewData(meal.getDate(), false);
                }
                return new GetHomeDataDAO.PreviewData(orderData.getDate(), true);
            })
            .collect(Collectors.toList());
        return new GetHomeDataDAO("", headerData, bodyData);
    }
}
