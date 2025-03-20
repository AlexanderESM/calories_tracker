package net.orekhov.calories_tracker.controller;

import net.orekhov.calories_tracker.entity.User;
import net.orekhov.calories_tracker.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Юнит-тесты для {@link UserController}.
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    /**
     * Тест получения списка пользователей.
     */
    @Test
    @DisplayName("GET /users - Должен вернуть список пользователей")
    void getUsers() throws Exception {
        List<User> mockUsers = List.of(
                new User("John Doe", "john@example.com", 30, 80.0, 180.0, User.Goal.MAINTAIN_WEIGHT),
                new User("Jane Doe", "jane@example.com", 25, 65.0, 170.0, User.Goal.LOSE_WEIGHT)
        );

        when(userService.getAllUsers()).thenReturn(mockUsers);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].name", is("John Doe")))
                .andExpect(jsonPath("$[1].name", is("Jane Doe")));
    }

    /**
     * Тест создания нового пользователя.
     */
    @Test
    @DisplayName("POST /users - Должен создать нового пользователя")
    void createUser() throws Exception {
        User user = new User("Alice", "alice@example.com", 28, 70.0, 175.0, User.Goal.GAIN_WEIGHT);

        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Alice",
                                  "email": "alice@example.com",
                                  "age": 28,
                                  "weight": 70.0,
                                  "height": 175.0,
                                  "goal": "GAIN_WEIGHT"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Alice")))
                .andExpect(jsonPath("$.email", is("alice@example.com")))
                .andExpect(jsonPath("$.age", is(28)))
                .andExpect(jsonPath("$.weight", is(70.0)))
                .andExpect(jsonPath("$.height", is(175.0)))
                .andExpect(jsonPath("$.goal", is("GAIN_WEIGHT")));
    }
}
