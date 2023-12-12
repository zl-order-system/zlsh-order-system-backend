package net.octoberserver.ordersystem.order;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Order {
    @Id
    UUID ID;
    UUID mealOptionID;
    long userID;
    LocalDate date;
    LunchBox lunchBox;
    boolean paid;
}
