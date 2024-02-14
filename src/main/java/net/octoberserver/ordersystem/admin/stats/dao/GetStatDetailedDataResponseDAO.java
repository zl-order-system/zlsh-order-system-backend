package net.octoberserver.ordersystem.admin.stats.dao;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetStatDetailedDataResponseDAO {
    private List<Short> personalLunchBox;
    private List<Short> schoolLunchBox;
}
