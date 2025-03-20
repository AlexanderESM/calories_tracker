package net.orekhov.calories_tracker.controller;

import net.orekhov.calories_tracker.entity.Food;
import net.orekhov.calories_tracker.exception.NotFoundException;
import net.orekhov.calories_tracker.service.FoodService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Юнит-тесты для {@link FoodController}.
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(FoodController.class)
class FoodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FoodService foodService;

    @Test
    @DisplayName("GET /foods - Должен вернуть список блюд")
    void getFoods() throws Exception {
        List<Food> mockFoods = List.of(new Food("Pizza", 300, 10.0, 12.0, 30.0));

        when(foodService.getAllFoods()).thenReturn(mockFoods);

        mockMvc.perform(get("/foods"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].name", is("Pizza")));
    }

    @Test
    @DisplayName("GET /foods/{name} - Должен вернуть блюдо по названию")
    void getFoodByName() throws Exception {
        Food food = new Food("Pizza", 300, 10.0, 12.0, 30.0);

        when(foodService.getFoodByName("Pizza")).thenReturn(food);

        mockMvc.perform(get("/foods/Pizza"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Pizza")))
                .andExpect(jsonPath("$.calories", is(300)));
    }

    @Test
    @DisplayName("GET /foods/{name} - Должен вернуть 404, если блюдо не найдено")
    void getFoodByName_NotFound() throws Exception {
        when(foodService.getFoodByName("Unknown")).thenThrow(new NotFoundException("Food not found"));

        mockMvc.perform(get("/foods/Unknown"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Food not found")));
    }

    @Test
    @DisplayName("POST /foods - Должен создать новое блюдо")
    void createFood() throws Exception {
        Food food = new Food("Burger", 500, 25.0, 30.0, 45.0);

        when(foodService.createFood(any(Food.class))).thenReturn(food);

        mockMvc.perform(post("/foods")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Burger",
                                  "calories": 500,
                                  "protein": 25.0,
                                  "fat": 30.0,
                                  "carbs": 45.0
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Burger")))
                .andExpect(jsonPath("$.calories", is(500)));
    }
}
