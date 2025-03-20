package net.orekhov.calories_tracker.repository;

import net.orekhov.calories_tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для управления сущностями {@link User}.
 * <p>
 * Использует Spring Data JPA для взаимодействия с базой данных.
 * </p>
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Находит пользователя по email.
     *
     * @param email Email пользователя.
     * @return Опциональный объект {@link User}, если найден.
     */
    Optional<User> findByEmail(String email);
}
