package net.octoberserver.ordersystem;

import org.springframework.stereotype.Service;

@Service
public class AppEnvService {
    // FRONTEND
    public final String FRONTEND_ROOT_URL = System.getenv("FRONTEND_ROOT_URL");

    // JWT
    public final String JWT_KEY = System.getenv("JWT_KEY");

    // MEAL
    public final String MEAL_AUTH_SECRET = System.getenv("MEAL_AUTH_SECRET");

    // LINE
    public final String LINE_CLIENT_ID = System.getenv("LINE_CLIENT_ID");
    public final String LINE_CLIENT_SECRET = System.getenv("LINE_CLIENT_SECRET");
    public final String LINE_TOKEN_URL = "https://notify-bot.line.me/oauth/token";
}
