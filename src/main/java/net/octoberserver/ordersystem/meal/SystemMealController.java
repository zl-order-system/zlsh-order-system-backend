package net.octoberserver.ordersystem.meal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.AppEnv;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/system/api/meal")
@RequiredArgsConstructor
public class SystemMealController {

    final MealRepository mealRepository;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class MealDAO {
        @NotNull
        private LocalDate date;
        @Valid
        @NotNull
        private List<MealOption> mealOptions;
    }

    @PutMapping
    ResponseEntity<Object> createMealData(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader, @RequestBody @Valid List<MealDAO> request) {
        if (!authHeader.equals("Bearer " + AppEnv.MEAL_AUTH_SECRET))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        mealRepository.saveAll(request.stream().map(dao -> new Meal(dao.getDate(), dao.getMealOptions(), false)).toList());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
