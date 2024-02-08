package net.octoberserver.ordersystem.admin.payments;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.admin.payments.dao.GetPaymentDataResponseDAO;
import net.octoberserver.ordersystem.admin.payments.dao.UpdatePaymentStatusRequestDAO;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/payments")
@RequiredArgsConstructor
public class PaymentsController {

    private final PaymentsService paymentsService;

    @GetMapping
    List<GetPaymentDataResponseDAO> getPaymentData(@RequestParam(name = "date") @FutureOrPresent @NotNull LocalDate date) {
        return paymentsService.getPaymentData(date);
    }

    @PatchMapping
    void updatePaymentStatus(@RequestBody @Valid UpdatePaymentStatusRequestDAO request) {
        paymentsService.updatePaymentStatus(request);
    }
}
