package net.orekhov.calories_tracker.service;

import net.orekhov.calories_tracker.entity.User;
import net.orekhov.calories_tracker.exception.NotFoundException;
import net.orekhov.calories_tracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User("John Doe", "john@example.com", 30, 80.0, 180.0, User.Goal.MAINTAIN_WEIGHT);
    }

    @Test
    @DisplayName("getAllUsers() - Должен вернуть список пользователей")
    void getAllUsers() {
        List<User> users = List.of(sampleUser);
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals(sampleUser, result.get(0));
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getUserById() - Должен вернуть пользователя по ID")
    void getUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        User result = userService.getUserById(1L);

        assertEquals(sampleUser, result);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("getUserById() - Должен выбросить NotFoundException, если пользователь не найден")
    void getUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.getUserById(1L));

        assertEquals("User with id 1 not found", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("createUser() - Должен создать нового пользователя")
    void createUser() {
        when(userRepository.findByEmail(sampleUser.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(sampleUser)).thenReturn(sampleUser);

        User result = userService.createUser(sampleUser);

        assertEquals(sampleUser, result);
        verify(userRepository, times(1)).findByEmail(sampleUser.getEmail());
        verify(userRepository, times(1)).save(sampleUser);
    }

    @Test
    @DisplayName("createUser() - Должен выбросить IllegalArgumentException, если email уже существует")
    void createUser_EmailExists() {
        when(userRepository.findByEmail(sampleUser.getEmail())).thenReturn(Optional.of(sampleUser));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(sampleUser));

        assertEquals("User with email 'john@example.com' already exists.", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(sampleUser.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }
}
