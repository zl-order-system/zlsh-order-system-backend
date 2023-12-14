package net.octoberserver.ordersystem.order;

import net.octoberserver.ordersystem.order.dto.MealOrderDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderData, UUID> {

    @Query("SELECT m, o FROM Meal m LEFT JOIN OrderData o ON m.date = o.date WHERE m.date > CURRENT_DATE ORDER BY m.date ASC")
    List<MealOrderDTO> findUpcomingMealsWithOrders();
}