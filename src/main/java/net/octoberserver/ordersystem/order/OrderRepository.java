package net.octoberserver.ordersystem.order;

import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderData, UUID> {
    @Query("""
        SELECT m, o FROM Meal m
        LEFT OUTER JOIN OrderData o
        ON m.date = o.date AND o.userID = :userID
        WHERE m.date >= CURRENT_DATE
        ORDER BY m.date
        LIMIT 5
    """)
    List<Tuple> findUpcomingMealsWithOrders(@Param("userID") Long userID);

    @Query("""
        SELECT u, o FROM AppUser u
        INNER JOIN OrderData o
        ON u.ID = o.userID
        WHERE o.date = :date
    """)
    List<Tuple> findOrdersWithUsersByDate(@Param("date") LocalDate date);

    Optional<OrderData> findByDateAndUserID(LocalDate date, long userID);

    @Query("""
        SELECT
            o.mealOption AS mealOption,
            SUM(CASE WHEN o.lunchBox = :personal THEN 1 ELSE 0 END) AS personalBoxCount,
            SUM(CASE WHEN o.lunchBox = :school THEN 1 ELSE 0 END) AS scholBoxCount
        FROM OrderData o
        WHERE o.date = :date
        GROUP BY o.mealOption
    """)
    List<Tuple> findStatData(@Param("date") LocalDate date, @Param("personal") LunchBox personal, @Param("school") LunchBox school);
}