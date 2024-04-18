package net.octoberserver.ordersystem.admin.payments;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.admin.payments.dao.GetPaymentDataResponseDAO;
import net.octoberserver.ordersystem.admin.payments.dao.UpdatePaymentStatusRequestDAO;
import net.octoberserver.ordersystem.user.AppUserRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static net.octoberserver.ordersystem.Utils.getUserFromCtx;

@RestController
@RequestMapping("/api/admin/payments")
@RequiredArgsConstructor
public class PaymentsController {

    private final PaymentsService paymentsService;
    private final AppUserRepository userRepository;

    @GetMapping
    GetPaymentDataResponseDAO getPaymentData(@RequestParam(name = "date") @FutureOrPresent @NotNull LocalDate date) {
        return paymentsService.getPaymentData(date, getUserFromCtx().getClassNumber());
    }

    @PatchMapping
    void updatePaymentStatus(@RequestBody @Valid UpdatePaymentStatusRequestDAO request) {
        paymentsService.updatePaymentStatus(request, getUserFromCtx().getClassNumber());
    }
}
