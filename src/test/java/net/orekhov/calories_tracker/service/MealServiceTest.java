package net.orekhov.calories_tracker.service;

import net.orekhov.calories_tracker.entity.Food;
import net.orekhov.calories_tracker.entity.Meal;
import net.orekhov.calories_tracker.entity.User;
import net.orekhov.calories_tracker.exception.NotFoundException;
import net.orekhov.calories_tracker.repository.MealRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MealServiceTest {

    @Mock
    private MealRepository mealRepository;

    @InjectMocks
    private MealService mealService;

    private Meal sampleMeal;
    private User sampleUser;
    private List<Food> sampleFoods;

    @BeforeEach
    void setUp() {
        sampleUser = new User("John Doe", "john@example.com", 30, 80.0, 180.0, User.Goal.MAINTAIN_WEIGHT);
        sampleFoods = List.of(new Food("Pizza", 300, 10.0, 12.0, 30.0));
        sampleMeal = new Meal(sampleUser, sampleFoods, LocalDateTime.now());
    }

    @Test
    @DisplayName("createMeal() - Должен создать новый прием пищи")
    void createMeal() {
        when(mealRepository.save(sampleMeal)).thenReturn(sampleMeal);

        Meal createdMeal = mealService.createMeal(sampleMeal);

        assertNotNull(createdMeal);
        assertEquals(sampleMeal, createdMeal);
        verify(mealRepository, times(1)).save(sampleMeal);
    }

    @Test
    @DisplayName("getMealById() - Должен вернуть прием пищи по ID")
    void getMealById() {
        when(mealRepository.findById(1L)).thenReturn(Optional.of(sampleMeal));

        Meal foundMeal = mealService.getMealById(1L);

        assertNotNull(foundMeal);
        assertEquals(sampleMeal, foundMeal);
        verify(mealRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("getMealById() - Должен выбросить NotFoundException, если прием пищи не найден")
    void getMealById_NotFound() {
        when(mealRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                mealService.getMealById(1L));

        assertEquals("Meal with id 1 not found", exception.getMessage());
        verify(mealRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("getUserMeals() - Должен вернуть список приемов пищи пользователя")
    void getUserMeals() {
        when(mealRepository.findByUser_IdOrderByDateTimeDesc(1L)).thenReturn(List.of(sampleMeal));

        List<Meal> meals = mealService.getUserMeals(1L);

        assertFalse(meals.isEmpty());
        assertEquals(1, meals.size());
        assertEquals(sampleMeal, meals.get(0));
        verify(mealRepository, times(1)).findByUser_IdOrderByDateTimeDesc(1L);
    }

    @Test
    @DisplayName("deleteMealById() - Должен удалить прием пищи по ID")
    void deleteMealById() {
        when(mealRepository.existsById(1L)).thenReturn(true);

        mealService.deleteMealById(1L);

        verify(mealRepository, times(1)).existsById(1L);
        verify(mealRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("deleteMealById() - Должен выбросить NotFoundException, если прием пищи не найден")
    void deleteMealById_NotFound() {
        when(mealRepository.existsById(1L)).thenReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                mealService.deleteMealById(1L));

        assertEquals("Meal with id 1 not found", exception.getMessage());
        verify(mealRepository, times(1)).existsById(1L);
        verify(mealRepository, never()).deleteById(1L);
    }
}
