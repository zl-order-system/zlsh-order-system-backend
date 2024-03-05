package net.octoberserver.ordersystem.admin.payments;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.admin.payments.dao.GetPaymentDataResponseDAO;
import net.octoberserver.ordersystem.admin.payments.dao.UpdatePaymentStatusRequestDAO;
import net.octoberserver.ordersystem.user.AppUserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/payments")
@RequiredArgsConstructor
public class PaymentsController {

    private final PaymentsService paymentsService;
    private final AppUserRepository userRepository;

    @GetMapping
    GetPaymentDataResponseDAO getPaymentData(@RequestParam(name = "date") @FutureOrPresent @NotNull LocalDate date) {
        final var userID = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        final var classNumber = userRepository.findById(userID).orElseThrow().getClassNumber();
        return paymentsService.getPaymentData(date, classNumber);
    }

    @PatchMapping
    void updatePaymentStatus(@RequestBody @Valid UpdatePaymentStatusRequestDAO request) {
        final var userID = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        final var classNumber = userRepository.findById(userID).orElseThrow().getClassNumber();
        paymentsService.updatePaymentStatus(request, classNumber);
    }
}
