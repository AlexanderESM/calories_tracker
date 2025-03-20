package net.orekhov.calories_tracker.service;

import net.orekhov.calories_tracker.entity.Meal;
import net.orekhov.calories_tracker.exception.NotFoundException;
import net.orekhov.calories_tracker.repository.MealRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Сервис для управления приемами пищи.
 */
@Service
public class MealService {
    private final MealRepository mealRepository;

    public MealService(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    @Transactional
    public Meal createMeal(Meal meal) {
        return mealRepository.save(meal);
    }

    public Meal getMealById(Long id) {
        return mealRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Meal with id " + id + " not found"));
    }

    public List<Meal> getUserMeals(Long userId) {
        return mealRepository.findByUser_IdOrderByDateTimeDesc(userId);
    }

    @Transactional
    public void deleteMealById(Long id) {
        if (!mealRepository.existsById(id)) {
            throw new NotFoundException("Meal with id " + id + " not found");
        }
        mealRepository.deleteById(id);
    }
}
