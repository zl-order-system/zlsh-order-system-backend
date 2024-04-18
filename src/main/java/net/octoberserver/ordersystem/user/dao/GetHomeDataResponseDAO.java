package net.octoberserver.ordersystem.user.dao;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetHomeDataResponseDAO {
    @Data
    @AllArgsConstructor
    public static class BannerData {
        private String today;
        private boolean hasPaidToday;
        private int owed;
        private int daysOrdered;
    }

    @Data
    @AllArgsConstructor
    public static class PreviewData {
        private String date;
        private boolean ordered;
    }

    private BannerData bannerData;
    private List<PreviewData> previewData;
}
