package net.orekhov.calories_tracker.service;

import net.orekhov.calories_tracker.entity.User;
import net.orekhov.calories_tracker.exception.NotFoundException;
import net.orekhov.calories_tracker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления пользователями.
 */
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Получает список всех пользователей.
     *
     * @return Список пользователей.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Получает пользователя по его ID.
     *
     * @param id ID пользователя.
     * @return Объект {@link User}, если найден.
     * @throws NotFoundException если пользователь не найден.
     */
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));
    }

    /**
     * Создает нового пользователя.
     * <p>
     * Если email уже существует, выбрасывается исключение.
     * </p>
     *
     * @param user Объект {@link User} для создания.
     * @return Созданный объект {@link User}.
     * @throws IllegalArgumentException если пользователь с таким email уже существует.
     */
    @Transactional
    public User createUser(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("User with email '" + user.getEmail() + "' already exists.");
        }
        return userRepository.save(user);
    }
}
