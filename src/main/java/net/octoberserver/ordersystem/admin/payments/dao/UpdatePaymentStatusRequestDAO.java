package net.octoberserver.ordersystem.admin.payments.dao;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

public record UpdatePaymentStatusRequestDAO(
    long userID,
    @NotNull
    @FutureOrPresent
    LocalDate date,
    boolean paid
) {}
