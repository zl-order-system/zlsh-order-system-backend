package net.octoberserver.ordersystem.admin.payments;

import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.admin.payments.dao.GetPaymentDataDAO;
import net.octoberserver.ordersystem.admin.payments.dao.PatchPaymentApproveDAO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/payments")
@RequiredArgsConstructor
public class PaymentsController {

    private final PaymentsService paymentsService;

    @GetMapping
    List<GetPaymentDataDAO.Response> getPaymentData(@RequestBody GetPaymentDataDAO.Request request) {
        return paymentsService.getPaymentData(request);
    }

    @PatchMapping
    void patchPaymentApprove(@RequestBody PatchPaymentApproveDAO request) {
        paymentsService.patchPaymentApprove(request);
    }
}
