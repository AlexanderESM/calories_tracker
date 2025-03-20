package net.orekhov.calories_tracker.controller;

import net.orekhov.calories_tracker.dto.MealRequestDto;
import net.orekhov.calories_tracker.entity.Food;
import net.orekhov.calories_tracker.entity.Meal;
import net.orekhov.calories_tracker.entity.User;
import net.orekhov.calories_tracker.exception.NotFoundException;
import net.orekhov.calories_tracker.repository.FoodRepository;
import net.orekhov.calories_tracker.repository.UserRepository;
import net.orekhov.calories_tracker.service.MealService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления приемами пищи.
 */
@RestController
@RequestMapping("/meals")
@Validated
public class MealController {
    private final MealService mealService;
    private final UserRepository userRepository;
    private final FoodRepository foodRepository;

    public MealController(MealService mealService, UserRepository userRepository, FoodRepository foodRepository) {
        this.mealService = mealService;
        this.userRepository = userRepository;
        this.foodRepository = foodRepository;
    }

    @PostMapping
    public ResponseEntity<Meal> createMeal(@RequestBody @Validated MealRequestDto mealRequest) {
        User user = userRepository.findById(mealRequest.getUserId())
                .orElseThrow(() -> new NotFoundException("User with id " + mealRequest.getUserId() + " not found"));

        List<Food> foods = foodRepository.findAllById(mealRequest.getFoodIds());

        if (foods.isEmpty() || foods.size() != mealRequest.getFoodIds().size()) {
            throw new NotFoundException("One or more foods not found");
        }

        Meal meal = new Meal(user, foods, null);
        Meal savedMeal = mealService.createMeal(meal);

        return ResponseEntity.status(201).body(savedMeal);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Meal> getMealById(@PathVariable Long id) {
        Meal meal = mealService.getMealById(id);
        return ResponseEntity.ok(meal);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Meal>> getUserMeals(@PathVariable Long userId) {
        List<Meal> meals = mealService.getUserMeals(userId);
        return meals.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(meals);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeal(@PathVariable Long id) {
        mealService.deleteMealById(id);
        return ResponseEntity.noContent().build();
    }
}
