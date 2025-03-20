package net.orekhov.calories_tracker.service;

import lombok.RequiredArgsConstructor;
import net.orekhov.calories_tracker.entity.Food;
import net.orekhov.calories_tracker.exception.NotFoundException;
import net.orekhov.calories_tracker.repository.FoodRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления блюдами.
 */
@Service
@RequiredArgsConstructor
public class FoodService {
    private final FoodRepository foodRepository;

    /**
     * Получает список всех блюд.
     *
     * @return Список объектов {@link Food}.
     */
    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }

    /**
     * Создает новое блюдо в системе.
     * Если блюдо с таким названием уже существует, возвращает существующее.
     *
     * @param food Объект {@link Food} для сохранения.
     * @return Созданный или существующий объект {@link Food}.
     */
    @Transactional
    public Food createFood(Food food) {
        return foodRepository.findFirstByNameIgnoreCase(food.getName())
                .orElseGet(() -> foodRepository.save(food));
    }

    /**
     * Получает блюдо по его названию.
     *
     * @param name Название блюда.
     * @return Объект {@link Food}, если найден.
     * @throws NotFoundException если блюдо не найдено.
     */
    public Food getFoodByName(String name) {
        return foodRepository.findFirstByNameIgnoreCase(name)
                .orElseThrow(() -> new NotFoundException("Food with name '" + name + "' not found"));
    }
}
