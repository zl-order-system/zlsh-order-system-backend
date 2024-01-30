package net.octoberserver.ordersystem.user.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class GetAccountDataResponseDAO {
    private String avatarUrl;
    private String name;
    private long id;
    private String googleName;
    private String email;
    private short classNumber;
    private short seatNumber;
}
