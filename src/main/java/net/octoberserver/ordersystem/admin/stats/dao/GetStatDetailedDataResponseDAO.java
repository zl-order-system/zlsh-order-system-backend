package net.octoberserver.ordersystem.admin.stats.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
public record GetStatDetailedDataResponseDAO(
    List<Short> personalLunchBox,
    List<Short> schoolLunchBox
) {}
