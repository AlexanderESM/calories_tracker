package net.orekhov.calories_tracker.controller;

import net.orekhov.calories_tracker.entity.Meal;
import net.orekhov.calories_tracker.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Контроллер для генерации отчетов по питанию пользователей.
 */
@RestController
@RequestMapping("/reports")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * Возвращает общее количество калорий, потребленных пользователем за текущий день.
     *
     * @param userId ID пользователя.
     * @return Map с ключом "totalCalories" и значением количества потребленных калорий.
     */
    @GetMapping("/{userId}/daily-calories")
    public ResponseEntity<Map<String, Integer>> getDailyCalories(@PathVariable Long userId) {
        int totalCalories = reportService.getDailyCalories(userId);
        return ResponseEntity.ok(Map.of("totalCalories", totalCalories));
    }

    /**
     * Проверяет, уложился ли пользователь в свою дневную норму калорий.
     *
     * @param userId ID пользователя.
     * @return Map с ключом "withinLimit" и значением true/false.
     */
    @GetMapping("/{userId}/within-daily-limit")
    public ResponseEntity<Map<String, Boolean>> isWithinDailyLimit(@PathVariable Long userId) {
        boolean withinLimit = reportService.isWithinDailyLimit(userId);
        return ResponseEntity.ok(Map.of("withinLimit", withinLimit));
    }

    /**
     * Возвращает историю приемов пищи пользователя.
     *
     * @param userId ID пользователя.
     * @return Список приемов пищи {@link Meal}, либо 404 Not Found, если история пуста.
     */
    @GetMapping("/{userId}/meal-history")
    public ResponseEntity<List<Meal>> getMealHistory(@PathVariable Long userId) {
        List<Meal> mealHistory = reportService.getMealHistory(userId);
        return mealHistory.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(mealHistory);
    }
}
