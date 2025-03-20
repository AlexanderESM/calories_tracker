package net.orekhov.calories_tracker.controller;

import lombok.RequiredArgsConstructor;
import net.orekhov.calories_tracker.entity.Food;
import net.orekhov.calories_tracker.exception.NotFoundException;
import net.orekhov.calories_tracker.service.FoodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления блюдами в системе.
 */
@RestController
@RequestMapping("/foods")
@RequiredArgsConstructor
public class FoodController {
    private final FoodService foodService;

    /**
     * Получает список всех блюд, доступных в системе.
     *
     * @return Список объектов {@link Food}.
     * @response 200 - Успешное получение списка блюд.
     * @response 500 - Ошибка сервера.
     */
    @GetMapping
    public ResponseEntity<List<Food>> getFoods() {
        return ResponseEntity.ok(foodService.getAllFoods());
    }

    /**
     * Получает информацию о блюде по названию.
     *
     * @param name Название блюда.
     * @return Найденный объект {@link Food}.
     * @throws NotFoundException если блюдо не найдено.
     */
    @GetMapping("/{name}")
    public ResponseEntity<Food> getFoodByName(@PathVariable String name) {
        return ResponseEntity.ok(foodService.getFoodByName(name));
    }

    /**
     * Создает новое блюдо в системе.
     *
     * @param food Объект {@link Food} в формате JSON.
     * @return Созданное или уже существующее блюдо.
     * @response 201 - Блюдо успешно создано или уже существует.
     * @response 500 - Ошибка сервера.
     */
    @PostMapping
    public ResponseEntity<Food> createFood(@RequestBody Food food) {
        Food createdFood = foodService.createFood(food);
        return ResponseEntity.status(201).body(createdFood);
    }
}
