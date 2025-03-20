package net.orekhov.calories_tracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * Сущность "Блюдо", содержащая информацию о калорийности и составе макронутриентов.
 */
@Entity
@Table(name = "foods")
public class Food {

    /**
     * Уникальный идентификатор блюда (автоматически генерируется).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Название блюда (не может быть пустым).
     */
    @NotBlank
    @Column(nullable = false)
    private String name;

    /**
     * Количество калорий в одной порции.
     */
    @Min(1)
    @Column(nullable = false)
    private int calories;

    /**
     * Количество белков (граммы).
     */
    @Min(0)
    @Column(nullable = false)
    private double protein;

    /**
     * Количество жиров (граммы).
     */
    @Min(0)
    @Column(nullable = false)
    private double fat;

    /**
     * Количество углеводов (граммы).
     */
    @Min(0)
    @Column(nullable = false)
    private double carbs;

    /**
     * Конструктор без аргументов (нужен для JPA).
     */
    public Food() {
    }

    /**
     * Конструктор для создания объекта Food.
     *
     * @param name     Название блюда.
     * @param calories Количество калорий.
     * @param protein  Белки (граммы).
     * @param fat      Жиры (граммы).
     * @param carbs    Углеводы (граммы).
     */
    public Food(String name, int calories, double protein, double fat, double carbs) {
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carbs = carbs;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getCarbs() {
        return carbs;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    /**
     * Переопределенный метод `toString()` для удобного вывода информации о блюде.
     *
     * @return Строковое представление объекта Food.
     */
    @Override
    public String toString() {
        return "Food{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", calories=" + calories +
                ", protein=" + protein +
                ", fat=" + fat +
                ", carbs=" + carbs +
                '}';
    }
}
