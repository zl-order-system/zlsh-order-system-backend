package net.octoberserver.ordersystem;

public class AppEnv {
    // FRONTEND
    public static final String FRONTEND_ROOT_URL = System.getenv("FRONTEND_ROOT_URL");

    // JWT
    public static final String JWT_KEY = System.getenv("JWT_KEY");

    // MEAL
    public static final String MEAL_AUTH_SECRET = System.getenv("MEAL_AUTH_SECRET");

    // LINE
    public static final String LINE_CLIENT_ID = System.getenv("LINE_CLIENT_ID");
    public static final String LINE_CLIENT_SECRET = System.getenv("LINE_CLIENT_SECRET");
    public static final String LINE_TOKEN_URL = "https://notify-bot.line.me/oauth/token";
}
