package net.orekhov.calories_tracker.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * DTO для создания нового приема пищи.
 */
public class MealRequestDto {

    /**
     * ID пользователя, который добавляет прием пищи.
     */
    @NotNull(message = "User ID cannot be null")
    private Long userId;

    /**
     * Список ID блюд, которые входят в прием пищи.
     */
    @NotEmpty(message = "Food IDs cannot be empty")
    private List<Long> foodIds;

    public MealRequestDto() {
    }

    public MealRequestDto(Long userId, List<Long> foodIds) {
        this.userId = userId;
        this.foodIds = foodIds;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getFoodIds() {
        return foodIds;
    }

    public void setFoodIds(List<Long> foodIds) {
        this.foodIds = foodIds;
    }
}
