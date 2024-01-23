package net.octoberserver.ordersystem.admin.payments;

import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.admin.payments.dao.GetPaymentDataDAO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/payments")
@RequiredArgsConstructor
public class PaymentsController {
    private final PaymentsService paymentsService;
    List<GetPaymentDataDAO.Response> getPaymentData(GetPaymentDataDAO.Request request) {
        return paymentsService.getPaymentData(request);
    }
}
