package net.octoberserver.ordersystem.admin.payments;

import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.admin.payments.dao.GetPaymentDataRequestDAO;
import net.octoberserver.ordersystem.admin.payments.dao.PatchPaymentApproveDAO;
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

    List<GetPaymentDataRequestDAO.Response> getPaymentData(GetPaymentDataRequestDAO.Request request) {
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
            return GetPaymentDataRequestDAO.Response.builder()
                .userID(user.getID())
                .name(user.getName())
                .seatNumber(user.getSeatNumber())
                .lunchBoxType(lunchBoxService.getLunchBoxString(order.getLunchBox()))
                .mealName(options.get(order.getMeal()))
                .paid(order.isPaid())
                .build();
        }).toList();
    }

    void patchPaymentApprove(PatchPaymentApproveDAO request) {
        final var orderData = orderRepository.findByDateAndUserID(request.getDate(), request.getUserID());
        orderData.setPaid(request.isPaid());
        orderRepository.save(orderData);
    }
}
