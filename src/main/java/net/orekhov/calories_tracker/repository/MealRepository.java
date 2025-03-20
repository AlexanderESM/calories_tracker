package net.orekhov.calories_tracker.repository;

import net.orekhov.calories_tracker.entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Репозиторий для управления сущностями {@link Meal}.
 * Использует Spring Data JPA.
 */
@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {

    /**
     * Находит все приемы пищи пользователя, отсортированные по дате убывания.
     *
     * @param userId ID пользователя.
     * @return Список приемов пищи, отсортированный по дате убывания.
     */
    List<Meal> findByUser_IdOrderByDateTimeDesc(Long userId);

    /**
     * Получает все приемы пищи пользователя за текущий день.
     *
     * @param userId     ID пользователя.
     * @param startOfDay Начало дня (00:00:00).
     * @param endOfDay   Конец дня (23:59:59).
     * @return Список приемов пищи за текущий день.
     */
    @Query("SELECT m FROM Meal m WHERE m.user.id = :userId AND m.dateTime BETWEEN :startOfDay AND :endOfDay")
    List<Meal> findMealsForUserToday(Long userId, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
