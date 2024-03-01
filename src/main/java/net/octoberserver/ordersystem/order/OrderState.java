package net.octoberserver.ordersystem.order;

public class OrderState {
    private OrderState() {}
    public static final String PAID = "已繳費";
    public static final String UNPAID = "未繳費";
    public static final String UNORDERED = "未訂餐";
}
