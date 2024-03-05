package net.octoberserver.ordersystem.admin.stats.dao;

import lombok.Builder;

@Builder
public record GetStatDataResponseDAO(
    short id, String name,
    long personalBoxCount,
    long schoolBoxCount
) {}
