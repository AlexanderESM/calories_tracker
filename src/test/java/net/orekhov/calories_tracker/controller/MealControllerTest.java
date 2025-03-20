package net.orekhov.calories_tracker.controller;

import net.orekhov.calories_tracker.dto.MealRequestDto;
import net.orekhov.calories_tracker.entity.Food;
import net.orekhov.calories_tracker.entity.Meal;
import net.orekhov.calories_tracker.entity.User;
import net.orekhov.calories_tracker.exception.NotFoundException;
import net.orekhov.calories_tracker.repository.FoodRepository;
import net.orekhov.calories_tracker.repository.UserRepository;
import net.orekhov.calories_tracker.service.MealService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Юнит-тесты для {@link MealController}.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class MealControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MealService mealService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private FoodRepository foodRepository;

    @Test
    @DisplayName("POST /meals - Должен создать новый прием пищи")
    void createMeal() throws Exception {
        // Подготовка данных
        MealRequestDto mealRequest = new MealRequestDto(1L, List.of(1L, 2L));
        User user = new User("John Doe", "john@example.com", 30, 80.0, 180.0, User.Goal.MAINTAIN_WEIGHT);

        // ✅ Исправлено: возвращаем **два** продукта
        List<Food> foods = List.of(
                new Food("Pizza", 300, 10.0, 12.0, 30.0),
                new Food("Salad", 150, 5.0, 3.0, 20.0)
        );

        Meal meal = new Meal(user, foods, LocalDateTime.now());

        // Моки
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(foodRepository.findAllById(List.of(1L, 2L))).thenReturn(foods); // ✅ Теперь 2 продукта
        when(mealService.createMeal(any(Meal.class))).thenReturn(meal);

        // Тестируем запрос
        mockMvc.perform(post("/meals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "userId": 1,
                              "foodIds": [1, 2]
                            }
                            """))
                .andExpect(status().isCreated()) // ✅ Ожидаем 201, а не 404
                .andExpect(jsonPath("$.user.name", is("John Doe")))
                .andExpect(jsonPath("$.foods[0].name", is("Pizza")))
                .andExpect(jsonPath("$.foods[1].name", is("Salad"))); // ✅ Проверяем оба продукта
    }

    @Test
    @DisplayName("GET /meals/{id} - Должен вернуть прием пищи по ID")
    void getMealById() throws Exception {
        User user = new User("John Doe", "john@example.com", 30, 80.0, 180.0, User.Goal.MAINTAIN_WEIGHT);
        List<Food> foods = List.of(new Food("Pizza", 300, 10.0, 12.0, 30.0));
        Meal meal = new Meal(user, foods, LocalDateTime.now());

        when(mealService.getMealById(1L)).thenReturn(meal);

        mockMvc.perform(get("/meals/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.name", is("John Doe")))
                .andExpect(jsonPath("$.foods[0].name", is("Pizza")));
    }

    @Test
    @DisplayName("GET /meals/user/{userId} - Должен вернуть список приемов пищи пользователя")
    void getUserMeals() throws Exception {
        User user = new User("John Doe", "john@example.com", 30, 80.0, 180.0, User.Goal.MAINTAIN_WEIGHT);
        List<Food> foods = List.of(new Food("Pizza", 300, 10.0, 12.0, 30.0));
        List<Meal> meals = List.of(new Meal(user, foods, LocalDateTime.now()));

        when(mealService.getUserMeals(1L)).thenReturn(meals);

        mockMvc.perform(get("/meals/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].user.name", is("John Doe")))
                .andExpect(jsonPath("$[0].foods[0].name", is("Pizza")));
    }

    @Test
    @DisplayName("DELETE /meals/{id} - Должен удалить прием пищи")
    void deleteMeal() throws Exception {
        doNothing().when(mealService).deleteMealById(1L);

        mockMvc.perform(delete("/meals/1"))
                .andExpect(status().isNoContent());

        verify(mealService, times(1)).deleteMealById(1L);
    }

    @Test
    @DisplayName("GET /meals/{id} - Должен вернуть 404, если прием пищи не найден")
    void getMealById_NotFound() throws Exception {
        when(mealService.getMealById(99L)).thenThrow(new NotFoundException("Meal not found"));

        mockMvc.perform(get("/meals/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Meal not found")));
    }
}
