package net.octoberserver.ordersystem.order;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.octoberserver.ordersystem.order.lunchbox.LunchBox;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class OrderData {
    @Id
    private UUID ID;
    private long userID;
    private short mealOption;
    private LocalDate date;
    @Enumerated(EnumType.STRING)
    private LunchBox lunchBox;
    private boolean paid;
}
