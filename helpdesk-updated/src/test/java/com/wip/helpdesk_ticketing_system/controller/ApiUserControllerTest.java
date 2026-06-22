package com.wip.helpdesk_ticketing_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.helpdesk_ticketing_system.dto.UserDto;
import com.wip.helpdesk_ticketing_system.entity.User;
import com.wip.helpdesk_ticketing_system.enums.Role;
import com.wip.helpdesk_ticketing_system.exception.UserNotFoundException;
import com.wip.helpdesk_ticketing_system.sevice.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApiUserController.class)
@DisplayName("ApiUserController Tests")
class ApiUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private User sampleUser;
    private UserDto sampleDto;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setUserId(1L);
        sampleUser.setName("Carol Admin");
        sampleUser.setEmail("carol@example.com");
        sampleUser.setRole(Role.ADMIN);
        sampleUser.setPasswordHash("$2a$hashed");

        sampleDto = new UserDto();
        sampleDto.setName("Carol Admin");
        sampleDto.setEmail("carol@example.com");
        sampleDto.setPassword("secret");
        sampleDto.setRole(Role.ADMIN);
    }

    // ---- POST /api/users ----

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/users: should return 201 CREATED when user is valid")
    void addUser_ShouldReturn201_WhenValid() throws Exception {
        when(userService.addUser(any(UserDto.class))).thenReturn(sampleUser);

        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.name").value("Carol Admin"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

//    @Test
//    @WithMockUser(roles = "AGENT")
//    @DisplayName("POST /api/users: should return 403 for non-ADMIN role")
//    void addUser_ShouldReturn403_ForNonAdmin() throws Exception {
//        mockMvc.perform(post("/api/users")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(sampleDto)))
//                .andExpect(status().isForbidden());
//    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/users: should return 400 when email is invalid")
    void addUser_ShouldReturn400_WhenEmailInvalid() throws Exception {
        sampleDto.setEmail("not-an-email");

        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleDto)))
                .andExpect(status().isBadRequest());
    }

    // ---- GET /api/users ----

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/users: should return list of users")
    void getAllUsers_ShouldReturn200_WithList() throws Exception {
        User user2 = new User();
        user2.setUserId(2L);
        user2.setName("Dave Agent");
        user2.setEmail("dave@example.com");
        user2.setRole(Role.AGENT);

        when(userService.getAllUsers()).thenReturn(List.of(sampleUser, user2));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Carol Admin"))
                .andExpect(jsonPath("$[1].name").value("Dave Agent"));
    }

//    @Test
//    @WithMockUser(roles = "END_USER")
//    @DisplayName("GET /api/users: should return 403 for END_USER")
//    void getAllUsers_ShouldReturn403_ForEndUser() throws Exception {
//        mockMvc.perform(get("/api/users"))
//                .andExpect(status().isForbidden());
//    }

    // ---- GET /api/users/{id} ----

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/users/{id}: should return user when found")
    void getUserById_ShouldReturn200_WhenFound() throws Exception {
        when(userService.getUserById(1L)).thenReturn(sampleUser);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.email").value("carol@example.com"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/users/{id}: should return 404 when user not found")
    void getUserById_ShouldReturn404_WhenNotFound() throws Exception {
        when(userService.getUserById(99L)).thenThrow(new UserNotFoundException(99L));

        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isNotFound());
    }

    // ---- PUT /api/users/{id} ----

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PUT /api/users/{id}: should return 200 and updated user")
    void updateUser_ShouldReturn200_WhenValid() throws Exception {
        User updated = new User();
        updated.setUserId(1L);
        updated.setName("Carol Updated");
        updated.setEmail("carol.updated@example.com");
        updated.setRole(Role.AGENT);

        when(userService.updateUser(eq(1L), any(UserDto.class))).thenReturn(updated);

        sampleDto.setName("Carol Updated");
        sampleDto.setEmail("carol.updated@example.com");
        sampleDto.setRole(Role.AGENT);

        mockMvc.perform(put("/api/users/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Carol Updated"))
                .andExpect(jsonPath("$.role").value("AGENT"));
    }

    // ---- DELETE /api/users/{id} ----

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /api/users/{id}: should return 200 success message")
    void deleteUser_ShouldReturn200_ForAdmin() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /api/users/{id}: should return 404 when user not found")
    void deleteUser_ShouldReturn404_WhenUserNotFound() throws Exception {
        doThrow(new UserNotFoundException(55L)).when(userService).deleteUser(55L);

        mockMvc.perform(delete("/api/users/55").with(csrf()))
                .andExpect(status().isNotFound());
    }
}
