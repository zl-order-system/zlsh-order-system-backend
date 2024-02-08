package net.octoberserver.ordersystem.meal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface MealRepository extends JpaRepository<Meal, LocalDate>  {
    @Query("SELECT m.date FROM Meal m WHERE m.date >= :today ORDER BY m.date ASC LIMIT 5")
    List<LocalDate> findUpcomingDates(LocalDate today);
}
