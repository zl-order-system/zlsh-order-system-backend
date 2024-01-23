package net.octoberserver.ordersystem.admin.payments;

import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.admin.payments.dao.GetPaymentDataDAO;
import net.octoberserver.ordersystem.order.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentsService {

    private final OrderRepository orderRepository;
    List<GetPaymentDataDAO.Response> getPaymentData(GetPaymentDataDAO.Request request) {
        // TODO: Get payment data:
        // 1. Where date = today
        // 2. Inner join
        return new ArrayList<>();
    }
}
