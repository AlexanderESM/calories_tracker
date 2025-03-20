package net.orekhov.calories_tracker.service;

import lombok.RequiredArgsConstructor;
import net.orekhov.calories_tracker.entity.Meal;
import net.orekhov.calories_tracker.entity.User;
import net.orekhov.calories_tracker.exception.NotFoundException;
import net.orekhov.calories_tracker.repository.MealRepository;
import net.orekhov.calories_tracker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Сервис для генерации отчетов по питанию пользователей.
 */
@Service
@RequiredArgsConstructor
public class ReportService {
    private final MealRepository mealRepository;
    private final UserRepository userRepository;

    /**
     * Вычисляет суммарное количество потребленных пользователем калорий за текущий день.
     *
     * @param userId ID пользователя.
     * @return Общее количество калорий за день.
     */
    @Transactional(readOnly = true)
    public int getDailyCalories(Long userId) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        List<Meal> meals = mealRepository.findMealsForUserToday(userId, startOfDay, endOfDay);

        return meals.stream().mapToInt(Meal::getTotalCalories).sum();
    }

    /**
     * Проверяет, уложился ли пользователь в свою дневную норму калорий.
     *
     * @param userId ID пользователя.
     * @return {@code true}, если потребленные калории не превышают дневную норму, иначе {@code false}.
     * @throws NotFoundException если пользователь не найден.
     */
    @Transactional(readOnly = true)
    public boolean isWithinDailyLimit(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        return getDailyCalories(userId) <= user.getDailyCalories();
    }

    /**
     * Получает историю приемов пищи пользователя, отсортированную по дате убывания.
     *
     * @param userId ID пользователя.
     * @return Список приемов пищи.
     * @throws NotFoundException если у пользователя нет приемов пищи.
     */
    @Transactional(readOnly = true)
    public List<Meal> getMealHistory(Long userId) {
        List<Meal> meals = mealRepository.findByUser_IdOrderByDateTimeDesc(userId);
        if (meals.isEmpty()) {
            throw new NotFoundException("No meals found for user with id " + userId);
        }
        return meals;
    }
}
