package net.octoberserver.ordersystem.order.dao;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class GetHomeDataDAO {
    @Data
    @AllArgsConstructor
    public static class BannerData {
        private LocalDate today;
        private boolean hasPaidToday;
        private int owed;
        private int daysOrdered;
    }

    @Data
    @AllArgsConstructor
    public static class PreviewData {
        private LocalDate date;
        private boolean ordered;
    }

    private String role;
    private BannerData bannerData;
    private List<PreviewData> previewData;
}
