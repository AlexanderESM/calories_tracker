package net.orekhov.calories_tracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/**
 * Сущность "Пользователь", содержащая информацию о личных данных и калорийности.
 */
@Entity
@Table(name = "users")
public class User {

    /**
     * Уникальный идентификатор пользователя (автоматически генерируется).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Имя пользователя (не может быть пустым).
     */
    @NotBlank
    @Column(nullable = false)
    private String name;

    /**
     * Email пользователя (уникальный, не может быть пустым).
     */
    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Возраст пользователя (не менее 18 лет).
     */
    @Min(18)
    @Column(nullable = false)
    private int age;

    /**
     * Вес пользователя (в кг, от 30 до 300).
     */
    @Min(30)
    @Max(300)
    @Column(nullable = false)
    private double weight;

    /**
     * Рост пользователя (в см, от 100 до 250).
     */
    @Min(100)
    @Max(250)
    @Column(nullable = false)
    private double height;

    /**
     * Цель пользователя (похудение, поддержание, набор массы).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Goal goal;

    /**
     * Дневная норма калорий (автоматически рассчитывается).
     */
    @Column(nullable = false)
    private int dailyCalories;

    /**
     * Перечисление целей пользователя.
     */
    public enum Goal {
        LOSE_WEIGHT, MAINTAIN_WEIGHT, GAIN_WEIGHT
    }

    /**
     * Конструктор без аргументов (нужен для JPA).
     */
    public User() {
    }

    /**
     * Конструктор для создания объекта User.
     *
     * @param name  Имя пользователя.
     * @param email Email пользователя.
     * @param age   Возраст пользователя.
     * @param weight Вес пользователя.
     * @param height Рост пользователя.
     * @param goal  Цель пользователя.
     */
    public User(String name, String email, int age, double weight, double height, Goal goal) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.goal = goal;
        calculateDailyCalories();
    }

    /**
     * Рассчитывает дневную норму калорий на основе формулы Харриса-Бенедикта.
     * Автоматически вызывается перед сохранением или обновлением записи в базе данных.
     */
    @PrePersist
    @PreUpdate
    public void calculateDailyCalories() {
        if (goal == null) {
            dailyCalories = 0;
            return;
        }

        double bmr = 10 * weight + 6.25 * height - 5 * age + 5; // Формула для мужчин

        switch (goal) {
            case LOSE_WEIGHT:
                dailyCalories = (int) (bmr * 1.2 - 500);
                break;
            case MAINTAIN_WEIGHT:
                dailyCalories = (int) (bmr * 1.2);
                break;
            case GAIN_WEIGHT:
                dailyCalories = (int) (bmr * 1.2 + 500);
                break;
        }
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
        calculateDailyCalories();
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
        calculateDailyCalories();
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
        calculateDailyCalories();
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
        calculateDailyCalories();
    }

    public int getDailyCalories() {
        return dailyCalories;
    }

    /**
     * Переопределенный метод `toString()` для удобного вывода информации о пользователе.
     *
     * @return Строковое представление объекта User.
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", weight=" + weight +
                ", height=" + height +
                ", goal=" + goal +
                ", dailyCalories=" + dailyCalories +
                '}';
    }
}
