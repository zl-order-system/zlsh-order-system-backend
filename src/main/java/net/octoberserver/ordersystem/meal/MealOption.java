package net.octoberserver.ordersystem.meal;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class MealOption {
    @Id
    String ID;
    LocalDate date;
    short number;
    String name;
}
