package net.orekhov.calories_tracker.controller;

import lombok.RequiredArgsConstructor;
import net.orekhov.calories_tracker.entity.User;
import net.orekhov.calories_tracker.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления пользователями.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Получает список всех пользователей.
     *
     * @return Список пользователей или `204 No Content`, если список пуст.
     */
    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.getAllUsers();
        return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
    }

    /**
     * Создает нового пользователя.
     *
     * @param user Объект {@link User} в формате JSON.
     * @return Созданный объект {@link User} с `201 Created`.
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(201).body(createdUser);
    }
}
