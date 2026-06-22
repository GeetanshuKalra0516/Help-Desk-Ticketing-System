package com.wip.helpdesk_ticketing_system.service;

import com.wip.helpdesk_ticketing_system.dto.UserDto;
import com.wip.helpdesk_ticketing_system.entity.User;
import com.wip.helpdesk_ticketing_system.enums.Role;
import com.wip.helpdesk_ticketing_system.exception.UserNotFoundException;
import com.wip.helpdesk_ticketing_system.repository.UserRepository;
import com.wip.helpdesk_ticketing_system.sevice.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserServiceImpl Tests")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User sampleUser;
    private UserDto sampleDto;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setUserId(1L);
        sampleUser.setName("Alice Smith");
        sampleUser.setEmail("alice@example.com");
        sampleUser.setPasswordHash("$2a$hashed");
        sampleUser.setRole(Role.AGENT);

        sampleDto = new UserDto();
        sampleDto.setName("Alice Smith");
        sampleDto.setEmail("alice@example.com");
        sampleDto.setPassword("password123");
        sampleDto.setRole(Role.AGENT);
    }

    // ---- addUser ----

    @Test
    @DisplayName("addUser: should create and return user when email is unique")
    void addUser_ShouldCreateUser_WhenEmailIsUnique() {
        when(userRepository.findByEmail("alice@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("$2a$hashed");
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        User result = userService.addUser(sampleDto);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Alice Smith");
        assertThat(result.getEmail()).isEqualTo("alice@example.com");
        assertThat(result.getRole()).isEqualTo(Role.AGENT);
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("addUser: should throw RuntimeException when email already exists")
    void addUser_ShouldThrowRuntimeException_WhenEmailAlreadyExists() {
        when(userRepository.findByEmail("alice@example.com")).thenReturn(Optional.of(sampleUser));

        assertThatThrownBy(() -> userService.addUser(sampleDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Email already exists");
        verify(userRepository, never()).save(any());
    }

    // ---- getAllUsers ----

    @Test
    @DisplayName("getAllUsers: should return all users")
    void getAllUsers_ShouldReturnAllUsers() {
        User user2 = new User();
        user2.setUserId(2L);
        user2.setName("Bob");
        user2.setEmail("bob@example.com");
        user2.setRole(Role.END_USER);

        when(userRepository.findAll()).thenReturn(List.of(sampleUser, user2));

        List<User> result = userService.getAllUsers();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Alice Smith");
        assertThat(result.get(1).getName()).isEqualTo("Bob");
    }

    @Test
    @DisplayName("getAllUsers: should return empty list when no users")
    void getAllUsers_ShouldReturnEmptyList_WhenNoUsers() {
        when(userRepository.findAll()).thenReturn(List.of());
        assertThat(userService.getAllUsers()).isEmpty();
    }

    // ---- getUserById ----

    @Test
    @DisplayName("getUserById: should return user when found")
    void getUserById_ShouldReturnUser_WhenFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        User result = userService.getUserById(1L);

        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo("alice@example.com");
    }

    @Test
    @DisplayName("getUserById: should throw UserNotFoundException when user not found")
    void getUserById_ShouldThrowUserNotFoundException_WhenNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(99L))
                .isInstanceOf(UserNotFoundException.class);
    }

    // ---- updateUser ----

    @Test
    @DisplayName("updateUser: should update name, email and role")
    void updateUser_ShouldUpdateUserFields() {
        UserDto updateDto = new UserDto();
        updateDto.setName("Alice Updated");
        updateDto.setEmail("alice.updated@example.com");
        updateDto.setPassword("");  // empty = keep existing
        updateDto.setRole(Role.ADMIN);

        User updatedUser = new User();
        updatedUser.setUserId(1L);
        updatedUser.setName("Alice Updated");
        updatedUser.setEmail("alice.updated@example.com");
        updatedUser.setRole(Role.ADMIN);

        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.updateUser(1L, updateDto);

        assertThat(result.getName()).isEqualTo("Alice Updated");
        assertThat(result.getRole()).isEqualTo(Role.ADMIN);
        verify(passwordEncoder, never()).encode(any()); // blank password should not encode
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("updateUser: should re-encode password when new password is provided")
    void updateUser_ShouldEncodeNewPassword_WhenProvided() {
        sampleDto.setPassword("newPassword456");
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.encode("newPassword456")).thenReturn("$2a$newHashed");
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        userService.updateUser(1L, sampleDto);

        verify(passwordEncoder).encode("newPassword456");
    }

    @Test
    @DisplayName("updateUser: should throw UserNotFoundException for missing user")
    void updateUser_ShouldThrow_WhenUserNotFound() {
        when(userRepository.findById(55L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.updateUser(55L, sampleDto))
                .isInstanceOf(UserNotFoundException.class);
    }

    // ---- deleteUser ----

    @Test
    @DisplayName("deleteUser: should call repository delete when user exists")
    void deleteUser_ShouldCallDelete_WhenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        userService.deleteUser(1L);

        verify(userRepository, times(1)).delete(sampleUser);
    }

    @Test
    @DisplayName("deleteUser: should throw UserNotFoundException when user not found")
    void deleteUser_ShouldThrow_WhenUserNotFound() {
        when(userRepository.findById(77L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.deleteUser(77L))
                .isInstanceOf(UserNotFoundException.class);
        verify(userRepository, never()).delete(any());
    }
}
