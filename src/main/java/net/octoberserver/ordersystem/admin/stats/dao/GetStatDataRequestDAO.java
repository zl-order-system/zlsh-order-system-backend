package net.octoberserver.ordersystem.admin.stats.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetStatDataRequestDAO {
    LocalDate date;
}
