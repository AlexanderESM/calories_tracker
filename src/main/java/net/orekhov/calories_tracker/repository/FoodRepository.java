package net.orekhov.calories_tracker.repository;

import net.orekhov.calories_tracker.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью {@link Food}.
 */
@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {

    /**
     * Находит продукт по названию (без учета регистра).
     *
     * @param name Название блюда.
     * @return Опциональный объект {@link Food}, если найден.
     */
    Optional<Food> findFirstByNameIgnoreCase(String name);
}
