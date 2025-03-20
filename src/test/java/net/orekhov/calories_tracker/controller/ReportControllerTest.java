package net.orekhov.calories_tracker.controller;

import net.orekhov.calories_tracker.entity.Food;
import net.orekhov.calories_tracker.entity.Meal;
import net.orekhov.calories_tracker.entity.User;
import net.orekhov.calories_tracker.service.ReportService;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Юнит-тесты для {@link ReportController}.
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(ReportController.class)
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    /**
     * Тестирует получение количества потребленных калорий за день.
     */
    @Test
    @DisplayName("GET /reports/{userId}/daily-calories - Должен вернуть общее количество калорий за день")
    void getDailyCalories() throws Exception {
        when(reportService.getDailyCalories(1L)).thenReturn(1800);

        mockMvc.perform(get("/reports/1/daily-calories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCalories", is(1800)));
    }

    /**
     * Тестирует проверку превышения дневного лимита калорий.
     */
    @Test
    @DisplayName("GET /reports/{userId}/within-daily-limit - Должен вернуть true, если пользователь в пределах дневного лимита")
    void isWithinDailyLimit() throws Exception {
        when(reportService.isWithinDailyLimit(1L)).thenReturn(true);

        mockMvc.perform(get("/reports/1/within-daily-limit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.withinLimit", is(true)));
    }

    /**
     * Тестирует получение истории приемов пищи пользователя.
     */
    @Test
    @DisplayName("GET /reports/{userId}/meal-history - Должен вернуть историю приемов пищи")
    void getMealHistory() throws Exception {
        User user = new User("John Doe", "john@example.com", 30, 80.0, 180.0, User.Goal.MAINTAIN_WEIGHT);
        List<Food> foods = List.of(new Food("Pizza", 300, 10.0, 12.0, 30.0));
        List<Meal> meals = List.of(new Meal(user, foods, LocalDateTime.now()));

        when(reportService.getMealHistory(1L)).thenReturn(meals);

        mockMvc.perform(get("/reports/1/meal-history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].user.name", is("John Doe")))
                .andExpect(jsonPath("$[0].foods[0].name", is("Pizza")));
    }
}
