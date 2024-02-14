package net.octoberserver.ordersystem.admin.stats.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetStatDataResponseDAO {
    private short id;
    private String name;
    private long personalBoxCount;
    private long schoolBoxCount;
}
