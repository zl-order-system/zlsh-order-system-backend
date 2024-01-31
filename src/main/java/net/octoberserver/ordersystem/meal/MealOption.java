package net.octoberserver.ordersystem.meal;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MealOption {
    @NotBlank
    private String name;
    private boolean schoolOnly;
}
