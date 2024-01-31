package net.octoberserver.ordersystem.admin.payments;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.admin.payments.dao.GetPaymentDataRequestDAO;
import net.octoberserver.ordersystem.admin.payments.dao.GetPaymentDataResponseDAO;
import net.octoberserver.ordersystem.admin.payments.dao.UpdatePaymentStatusRequestDAO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/payments")
@RequiredArgsConstructor
public class PaymentsController {

    private final PaymentsService paymentsService;

    @GetMapping
    List<GetPaymentDataResponseDAO> getPaymentData(@RequestBody @Valid GetPaymentDataRequestDAO request) {
        return paymentsService.getPaymentData(request);
    }

    @PatchMapping
    void updatePaymentStatus(@RequestBody @Valid UpdatePaymentStatusRequestDAO request) {
        paymentsService.updatePaymentStatus(request);
    }
}
