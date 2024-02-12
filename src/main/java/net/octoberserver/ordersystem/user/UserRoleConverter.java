package net.octoberserver.ordersystem.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import net.octoberserver.ordersystem.meal.MealOption;

import java.util.List;

@Converter
public class UserRoleConverter implements AttributeConverter<List<Role>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Role> list) {
        try {
            return objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Role> convertToEntityAttribute(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, new TypeReference<List<Role>>() {});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
