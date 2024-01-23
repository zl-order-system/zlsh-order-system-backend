package net.octoberserver.ordersystem.meal;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Meal {
    @Id
    private LocalDate date;
    private String options;

    public List<MealOptionDTO> getOptions() {
        try {
            return new ObjectMapper().readValue(options, ArrayList.class);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setOptions(List<MealOptionDTO> options) {
        try {
            this.options = new ObjectMapper().writeValueAsString(options);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Deprecated(forRemoval=true)
    public void setOptions(String s) {}
}
