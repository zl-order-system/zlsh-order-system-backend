package net.octoberserver.ordersystem.admin.stats.dao;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetStatDataRequestDAO {
    @Future
    @NotNull
    private LocalDate date;
}
