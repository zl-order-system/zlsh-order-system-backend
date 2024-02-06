package net.octoberserver.ordersystem.meal;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/system/api/meal")
@RequiredArgsConstructor
public class SystemMealController {

    final MealRepository mealRepository;

    @Value("#{environment.MEAL_AUTH_SECRET}")
    private String mealAuthSecret;

    @PutMapping
    ResponseEntity<Object> createMealData(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader, @RequestBody List<Meal> request) {
        if (!authHeader.equals("Bearer " + mealAuthSecret))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        mealRepository.saveAll(request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
