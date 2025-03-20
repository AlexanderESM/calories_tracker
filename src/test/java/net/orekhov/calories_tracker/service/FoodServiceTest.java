package net.orekhov.calories_tracker.service;

import net.orekhov.calories_tracker.entity.Food;
import net.orekhov.calories_tracker.exception.NotFoundException;
import net.orekhov.calories_tracker.repository.FoodRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FoodServiceTest {

    @Mock
    private FoodRepository foodRepository;

    @InjectMocks
    private FoodService foodService;

    private Food sampleFood;

    @BeforeEach
    void setUp() {
        sampleFood = new Food("Pizza", 300, 10.0, 12.0, 30.0);
    }

    @Test
    @DisplayName("getAllFoods() - Должен вернуть список всех блюд")
    void getAllFoods() {
        List<Food> mockFoods = List.of(sampleFood);
        when(foodRepository.findAll()).thenReturn(mockFoods);

        List<Food> result = foodService.getAllFoods();

        assertEquals(1, result.size());
        assertEquals("Pizza", result.get(0).getName());
        verify(foodRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("createFood() - Должен создать новое блюдо")
    void createFood_NewFood() {
        when(foodRepository.findFirstByNameIgnoreCase("Pizza")).thenReturn(Optional.empty());
        when(foodRepository.save(sampleFood)).thenReturn(sampleFood);

        Food createdFood = foodService.createFood(sampleFood);

        assertNotNull(createdFood);
        assertEquals("Pizza", createdFood.getName());
        verify(foodRepository, times(1)).findFirstByNameIgnoreCase("Pizza");
        verify(foodRepository, times(1)).save(sampleFood);
    }

    @Test
    @DisplayName("createFood() - Должен вернуть существующее блюдо")
    void createFood_ExistingFood() {
        when(foodRepository.findFirstByNameIgnoreCase("Pizza")).thenReturn(Optional.of(sampleFood));

        Food existingFood = foodService.createFood(sampleFood);

        assertNotNull(existingFood);
        assertEquals("Pizza", existingFood.getName());
        verify(foodRepository, times(1)).findFirstByNameIgnoreCase("Pizza");
        verify(foodRepository, never()).save(any(Food.class));  // Save не должен вызываться
    }

    @Test
    @DisplayName("getFoodByName() - Должен вернуть блюдо по названию")
    void getFoodByName_Found() {
        when(foodRepository.findFirstByNameIgnoreCase("Pizza")).thenReturn(Optional.of(sampleFood));

        Food foundFood = foodService.getFoodByName("Pizza");

        assertNotNull(foundFood);
        assertEquals("Pizza", foundFood.getName());
        verify(foodRepository, times(1)).findFirstByNameIgnoreCase("Pizza");
    }

    @Test
    @DisplayName("getFoodByName() - Должен выбросить NotFoundException, если блюдо не найдено")
    void getFoodByName_NotFound() {
        when(foodRepository.findFirstByNameIgnoreCase("Unknown")).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                foodService.getFoodByName("Unknown"));

        assertEquals("Food with name 'Unknown' not found", exception.getMessage());
        verify(foodRepository, times(1)).findFirstByNameIgnoreCase("Unknown");
    }
}
