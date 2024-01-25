package net.octoberserver.ordersystem.admin.payments;

import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.admin.payments.dao.GetPaymentDataDAO;
import net.octoberserver.ordersystem.common.LunchBoxService;
import net.octoberserver.ordersystem.meal.MealOption;
import net.octoberserver.ordersystem.meal.MealRepository;
import net.octoberserver.ordersystem.order.OrderData;
import net.octoberserver.ordersystem.order.OrderRepository;
import net.octoberserver.ordersystem.user.AppUser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentsService {

    private final OrderRepository orderRepository;
    private final MealRepository mealRepository;
    private final LunchBoxService lunchBoxService;

    List<GetPaymentDataDAO.Response> getPaymentData(GetPaymentDataDAO.Request request) {
        final var options = mealRepository
            .findById(request.getDate())
            .orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Data not found"))
            .getOptions()
            .stream().map(MealOption::getName)
            .toList();

        return orderRepository.findOrdersWithUsersByDate(request.getDate()).stream().map(userOrder -> {
            final var user = userOrder.get(0, AppUser.class);
            final var order = userOrder.get(1, OrderData.class);
            return GetPaymentDataDAO.Response.builder()
                .id(user.getID())
                .name(user.getName())
                .seatNumber(user.getSeatNumber())
                .lunchBoxType(lunchBoxService.getLunchBoxString(order.getLunchBox()))
                .mealName(options.get(order.getMeal()))
                .paid(order.isPaid())
                .build();
        }).toList();
    }
}
