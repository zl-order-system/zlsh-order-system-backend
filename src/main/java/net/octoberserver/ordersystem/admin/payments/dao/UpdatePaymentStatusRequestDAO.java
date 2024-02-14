package net.octoberserver.ordersystem.admin.payments.dao;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePaymentStatusRequestDAO {
    private long userID;
    @FutureOrPresent
    @NotNull
    private LocalDate date;
    private boolean paid;
}
