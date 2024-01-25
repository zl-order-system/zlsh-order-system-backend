package net.octoberserver.ordersystem.admin.payments.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatchPaymentApproveDAO {
    private long userID;
    private LocalDate date;
    private boolean paid;
}
