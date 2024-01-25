package net.octoberserver.ordersystem.meal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MealOption {
    private String name;
    private boolean schoolOnly;
}
