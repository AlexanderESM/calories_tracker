package net.orekhov.calories_tracker.service;

import net.orekhov.calories_tracker.entity.Food;
import net.orekhov.calories_tracker.entity.Meal;
import net.orekhov.calories_tracker.entity.User;
import net.orekhov.calories_tracker.exception.NotFoundException;
import net.orekhov.calories_tracker.repository.MealRepository;
import net.orekhov.calories_tracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private MealRepository mealRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReportService reportService;

    private User sampleUser;
    private Meal sampleMeal;
    private List<Food> sampleFoods;

    @BeforeEach
    void setUp() {
        sampleUser = new User("John Doe", "john@example.com", 30, 80.0, 180.0, User.Goal.MAINTAIN_WEIGHT);
        sampleFoods = List.of(new Food("Pizza", 500, 10.0, 20.0, 50.0));
        sampleMeal = new Meal(sampleUser, sampleFoods, LocalDateTime.now());
    }

    @Test
    @DisplayName("getDailyCalories() - Должен вернуть общее количество потребленных калорий за день")
    void getDailyCalories() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        when(mealRepository.findMealsForUserToday(1L, startOfDay, endOfDay)).thenReturn(List.of(sampleMeal));

        int dailyCalories = reportService.getDailyCalories(1L);

        assertEquals(500, dailyCalories);
        verify(mealRepository, times(1)).findMealsForUserToday(1L, startOfDay, endOfDay);
    }

    @Test
    @DisplayName("isWithinDailyLimit() - Должен вернуть true, если калории в пределах нормы")
    void isWithinDailyLimit_WithinLimit() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        // Создаем список приемов пищи, где общее количество калорий = 1500
        List<Meal> meals = List.of(
                new Meal(sampleUser, List.of(new Food("Pizza", 500, 10.0, 20.0, 50.0)), LocalDateTime.now()),
                new Meal(sampleUser, List.of(new Food("Burger", 1000, 20.0, 30.0, 60.0)), LocalDateTime.now())
        );

        when(mealRepository.findMealsForUserToday(anyLong(), any(), any())).thenReturn(meals);

        boolean result = reportService.isWithinDailyLimit(1L);

        assertTrue(result);
        verify(userRepository, times(1)).findById(1L);
        verify(mealRepository, times(1)).findMealsForUserToday(eq(1L), any(), any());
    }

    @Test
    @DisplayName("isWithinDailyLimit() - Должен вернуть false, если калории превышают норму")
    void isWithinDailyLimit_OverLimit() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        // Создаем список приемов пищи, где общее количество калорий = 2500
        List<Meal> meals = List.of(
                new Meal(sampleUser, List.of(new Food("Pizza", 1000, 10.0, 20.0, 50.0)), LocalDateTime.now()),
                new Meal(sampleUser, List.of(new Food("Burger", 1500, 20.0, 30.0, 60.0)), LocalDateTime.now())
        );

        when(mealRepository.findMealsForUserToday(anyLong(), any(), any())).thenReturn(meals);

        boolean result = reportService.isWithinDailyLimit(1L);

        assertFalse(result);
        verify(userRepository, times(1)).findById(1L);
        verify(mealRepository, times(1)).findMealsForUserToday(eq(1L), any(), any());
    }


    @Test
    @DisplayName("isWithinDailyLimit() - Должен выбросить NotFoundException, если пользователь не найден")
    void isWithinDailyLimit_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                reportService.isWithinDailyLimit(1L));

        assertEquals("User with id 1 not found", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("getMealHistory() - Должен вернуть историю приемов пищи пользователя")
    void getMealHistory() {
        when(mealRepository.findByUser_IdOrderByDateTimeDesc(1L)).thenReturn(List.of(sampleMeal));

        List<Meal> meals = reportService.getMealHistory(1L);

        assertFalse(meals.isEmpty());
        assertEquals(1, meals.size());
        assertEquals(sampleMeal, meals.get(0));
        verify(mealRepository, times(1)).findByUser_IdOrderByDateTimeDesc(1L);
    }

    @Test
    @DisplayName("getMealHistory() - Должен выбросить NotFoundException, если у пользователя нет приемов пищи")
    void getMealHistory_NotFound() {
        when(mealRepository.findByUser_IdOrderByDateTimeDesc(1L)).thenReturn(List.of());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                reportService.getMealHistory(1L));

        assertEquals("No meals found for user with id 1", exception.getMessage());
        verify(mealRepository, times(1)).findByUser_IdOrderByDateTimeDesc(1L);
    }
}
