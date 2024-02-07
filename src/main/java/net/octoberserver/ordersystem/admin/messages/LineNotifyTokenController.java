package net.octoberserver.ordersystem.admin.messages;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.http.HttpHeaders;
import java.util.HashMap;

@Controller
@RequestMapping("/api/admin/messages")
@RequiredArgsConstructor
public class LineNotifyTokenController {
//    @Value("#{environment.LINE_CLIENT_SECRET}")
//    private final String LINE_CLIENT_SECRET = "";
//    private static final String LINE_TOKEN_URL = "https://notify-bot.line.me/oauth/token";
//
//    @Data
//    @NoArgsConstructor
//    @AllArgsConstructor
//    private static class LineTokenRequestDAO {
//        @NotBlank
//        private String code;
//    }
//    @PutMapping
//    void updateOrCreateLineToken(@RequestBody @Valid LineTokenRequestDAO request) {
//        // TODO: Get token from Line using code
//        // TODO: Save token in DB
//    }
}
