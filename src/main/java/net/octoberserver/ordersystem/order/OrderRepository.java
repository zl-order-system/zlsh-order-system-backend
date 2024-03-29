package net.octoberserver.ordersystem.order;

import jakarta.persistence.Tuple;
import net.octoberserver.ordersystem.order.lunchbox.LunchBox;
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
        AND u.classNumber = :classNum
    """)
    List<Tuple> findOrdersWithUsersByDate(@Param("date") LocalDate date, @Param("classNum") short classNumber);

    @Query("""
        SELECT
            o.mealOption AS mealOption,
            SUM(CASE WHEN o.lunchBox = :personal THEN 1 ELSE 0 END) AS personalBoxCount,
            SUM(CASE WHEN o.lunchBox = :school THEN 1 ELSE 0 END) AS scholBoxCount
        FROM OrderData o
        INNER JOIN AppUser u
        ON o.userID = u.ID
        WHERE o.date = :date
        AND u.classNumber = :classNum
        AND o.paid = true
        GROUP BY o.mealOption
    """)
    List<Tuple> findStatData(@Param("date") LocalDate date, @Param("classNum") short classNumber, @Param("personal") LunchBox personal, @Param("school") LunchBox school);

    @Query("""
        SELECT u.seatNumber, o.lunchBox
        FROM OrderData o
        INNER JOIN AppUser u
        ON u.ID = o.userID
        WHERE o.date = :date
        AND u.classNumber = :classNum
        AND o.paid = true
        AND o.mealOption = :mealOption
    """)
    List<Tuple> findStatDetailed(@Param("date") LocalDate date, @Param("classNum") short classNumber, @Param("mealOption") short mealOption);

    Optional<OrderData> findByDateAndUserID(LocalDate date, long userID);

    Optional<OrderData> findFirstByDate(LocalDate date);
}