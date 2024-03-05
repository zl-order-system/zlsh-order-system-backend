package net.octoberserver.ordersystem.user.dao;

import lombok.Builder;

@Builder
public record GetAccountDataResponseDAO(
    long id,
    String avatarUrl,
    String name,
    String googleName,
    String email,
    short classNumber,
    short seatNumber
) {}
