package net.octoberserver.ordersystem.order;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LunchBoxService {
    public int getPrice(LunchBox lunchBox) {
        return switch (lunchBox) {
            case PERSONAL -> 65;
            case SCHOOL -> 70;
        };
    }

    public String getLunchBoxString(LunchBox lunchBox) {
        return switch (lunchBox) {
            case PERSONAL -> LunchBoxType.PERSONAL;
            case SCHOOL -> LunchBoxType.SCHOOL;
        };
    }

    public LunchBox getLunchBoxEnum(String string) {
        return switch (string) {
            case LunchBoxType.PERSONAL -> LunchBox.PERSONAL;
            case LunchBoxType.SCHOOL -> LunchBox.SCHOOL;
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Lunch Box Type: " + string);
        };
    }
}
