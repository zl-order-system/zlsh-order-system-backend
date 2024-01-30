package net.octoberserver.ordersystem.user;

import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.meal.Meal;
import net.octoberserver.ordersystem.order.LunchBoxService;
import net.octoberserver.ordersystem.order.OrderData;
import net.octoberserver.ordersystem.order.OrderRepository;
import net.octoberserver.ordersystem.user.dao.GetAccountDataResponseDAO;
import net.octoberserver.ordersystem.user.dao.GetHomeDataResponseDAO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final LunchBoxService lunchBoxService;
    private final AppUserRepository userRepository;
    private final OrderRepository orderRepository;

    GetHomeDataResponseDAO getHomeData(long userID) {
        final var result = processHomeData(orderRepository.findUpcomingMealsWithOrders(userID), LocalDate.now());
        result.setRole(
            userRepository
                .findById(userID)
                .orElseThrow()
                .getRole()
                .name()
        );
        return result;
    }

    GetAccountDataResponseDAO getAccountData(long userID) {
        final var user = userRepository.findById(userID).orElseThrow();
        return GetAccountDataResponseDAO.builder()
            .avatarUrl("")
            .name(user.getName())
            .id(userID)
            .googleName(user.googleName)
            .email(user.getEmail())
            .classNumber(user.getClassNumber())
            .seatNumber(user.getSeatNumber())
            .build();
    }

    public GetHomeDataResponseDAO processHomeData(List<Tuple> mealOrders, LocalDate today) {
        final var headerData = new GetHomeDataResponseDAO.BannerData(today, false, 0, 0);
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
                    return new GetHomeDataResponseDAO.PreviewData(meal.getDate(), false);
                }
                return new GetHomeDataResponseDAO.PreviewData(orderData.getDate(), true);
            })
            .collect(Collectors.toList());
        return new GetHomeDataResponseDAO("", headerData, bodyData);
    }
}
