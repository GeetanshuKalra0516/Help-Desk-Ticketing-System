package com.wip.helpdesk_ticketing_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.helpdesk_ticketing_system.dto.TicketDto;
import com.wip.helpdesk_ticketing_system.entity.Ticket;
import com.wip.helpdesk_ticketing_system.entity.User;
import com.wip.helpdesk_ticketing_system.enums.Priority;
import com.wip.helpdesk_ticketing_system.enums.Role;
import com.wip.helpdesk_ticketing_system.enums.Status;
import com.wip.helpdesk_ticketing_system.exception.TicketNotFoundException;
import com.wip.helpdesk_ticketing_system.sevice.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApiTicketController.class)
@DisplayName("ApiTicketController Tests")
class ApiTicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TicketService ticketService;

    private Ticket sampleTicket;
    private TicketDto sampleDto;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setUserId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setRole(Role.END_USER);

        sampleTicket = new Ticket();
        sampleTicket.setTicketId(1L);
        sampleTicket.setTitle("Screen flickering");
        sampleTicket.setDescription("Monitor flickers every few seconds.");
        sampleTicket.setPriority(Priority.HIGH);
        sampleTicket.setStatus(Status.OPEN);
        sampleTicket.setCreatedDate(LocalDateTime.now());
        sampleTicket.setUpdatedDate(LocalDateTime.now());
        sampleTicket.setUser(user);

        sampleDto = new TicketDto();
        sampleDto.setTitle("Screen flickering");
        sampleDto.setDescription("Monitor flickers every few seconds.");
        sampleDto.setPriority(Priority.HIGH);
        sampleDto.setUserId(1L);
    }

    // ---- POST /api/tickets ----

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/tickets: should return 201 CREATED when ticket is valid")
    void createTicket_ShouldReturn201_WhenValid() throws Exception {
        when(ticketService.createTicket(any(TicketDto.class))).thenReturn(sampleTicket);

        mockMvc.perform(post("/api/tickets")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ticketId").value(1))
                .andExpect(jsonPath("$.title").value("Screen flickering"))
                .andExpect(jsonPath("$.status").value("OPEN"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/tickets: should return 400 when title is blank")
    void createTicket_ShouldReturn400_WhenTitleIsBlank() throws Exception {
        sampleDto.setTitle("");

        mockMvc.perform(post("/api/tickets")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleDto)))
                .andExpect(status().isBadRequest());
    }

    // ---- GET /api/tickets ----

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/tickets: should return list of all tickets")
    void getAllTickets_ShouldReturn200_WithList() throws Exception {
        when(ticketService.getAllTickets()).thenReturn(List.of(sampleTicket));

        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Screen flickering"));
    }

//    @Test
//    @WithMockUser(roles = "END_USER")
//    @DisplayName("GET /api/tickets: should return 403 for END_USER role")
//    void getAllTickets_ShouldReturn403_ForEndUser() throws Exception {
//        mockMvc.perform(get("/api/tickets"))
//                .andExpect(status().isForbidden());
//    }

    // ---- GET /api/tickets/{id} ----

    @Test
    @WithMockUser(roles = "AGENT")
    @DisplayName("GET /api/tickets/{id}: should return ticket when it exists")
    void getTicketById_ShouldReturn200_WhenFound() throws Exception {
        when(ticketService.getTicketById(1L)).thenReturn(sampleTicket);

        mockMvc.perform(get("/api/tickets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketId").value(1))
                .andExpect(jsonPath("$.priority").value("HIGH"));
    }

    @Test
    @WithMockUser(roles = "AGENT")
    @DisplayName("GET /api/tickets/{id}: should return 404 when ticket not found")
    void getTicketById_ShouldReturn404_WhenNotFound() throws Exception {
        when(ticketService.getTicketById(999L)).thenThrow(new TicketNotFoundException(999L));

        mockMvc.perform(get("/api/tickets/999"))
                .andExpect(status().isNotFound());
    }

    // ---- PUT /api/tickets/{id} ----

    @Test
    @WithMockUser(roles = "AGENT")
    @DisplayName("PUT /api/tickets/{id}: should return 200 and updated ticket")
    void updateTicket_ShouldReturn200_WhenValid() throws Exception {
        Ticket updated = new Ticket();
        updated.setTicketId(1L);
        updated.setTitle("Screen flickering - updated");
        updated.setDescription("Still flickering after restart.");
        updated.setPriority(Priority.CRITICAL);
        updated.setStatus(Status.IN_PROGRESS);
        updated.setUser(sampleTicket.getUser());

        when(ticketService.updateTicket(eq(1L), any(TicketDto.class))).thenReturn(updated);

        sampleDto.setTitle("Screen flickering - updated");
        sampleDto.setPriority(Priority.CRITICAL);

        mockMvc.perform(put("/api/tickets/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Screen flickering - updated"))
                .andExpect(jsonPath("$.priority").value("CRITICAL"));
    }

    // ---- PATCH /api/tickets/{id}/status ----

    @Test
    @WithMockUser(roles = "AGENT")
    @DisplayName("PATCH /api/tickets/{id}/status: should update status and return 200")
    void updateStatus_ShouldReturn200() throws Exception {
        Ticket statusUpdated = new Ticket();
        statusUpdated.setTicketId(1L);
        statusUpdated.setStatus(Status.RESOLVED);
        statusUpdated.setUser(sampleTicket.getUser());
        statusUpdated.setTitle(sampleTicket.getTitle());
        statusUpdated.setDescription(sampleTicket.getDescription());
        statusUpdated.setPriority(sampleTicket.getPriority());

        when(ticketService.updateStatus(1L, Status.RESOLVED)).thenReturn(statusUpdated);

        mockMvc.perform(patch("/api/tickets/1/status")
                        .with(csrf())
                        .param("status", "RESOLVED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("RESOLVED"));
    }

    // ---- DELETE /api/tickets/{id} ----

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /api/tickets/{id}: should return 200 with success message")
    void deleteTicket_ShouldReturn200_ForAdmin() throws Exception {
        doNothing().when(ticketService).deleteTicket(1L);

        mockMvc.perform(delete("/api/tickets/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Ticket deleted successfully"));
    }

//    @Test
//    @WithMockUser(roles = "AGENT")
//    @DisplayName("DELETE /api/tickets/{id}: should return 403 for AGENT role")
//    void deleteTicket_ShouldReturn403_ForAgent() throws Exception {
//        mockMvc.perform(delete("/api/tickets/1").with(csrf()))
//                .andExpect(status().isForbidden());
//    }

    // ---- GET /api/tickets/user/{userId} ----

    @Test
    @WithMockUser(roles = "END_USER")
    @DisplayName("GET /api/tickets/user/{userId}: should return tickets for user")
    void getTicketsByUser_ShouldReturn200() throws Exception {
        when(ticketService.getTicketsByUser(1L)).thenReturn(List.of(sampleTicket));

        mockMvc.perform(get("/api/tickets/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    // ---- GET /api/tickets/status/{status} ----

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/tickets/status/{status}: should return tickets filtered by status")
    void getTicketsByStatus_ShouldReturn200() throws Exception {
        when(ticketService.getTicketsByStatus(Status.OPEN)).thenReturn(List.of(sampleTicket));

        mockMvc.perform(get("/api/tickets/status/OPEN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("OPEN"));
    }
}
