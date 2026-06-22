package com.wip.helpdesk_ticketing_system.service;

import com.wip.helpdesk_ticketing_system.dto.TicketDto;
import com.wip.helpdesk_ticketing_system.entity.Ticket;
import com.wip.helpdesk_ticketing_system.entity.User;
import com.wip.helpdesk_ticketing_system.enums.Priority;
import com.wip.helpdesk_ticketing_system.enums.Role;
import com.wip.helpdesk_ticketing_system.enums.Status;
import com.wip.helpdesk_ticketing_system.exception.TicketNotFoundException;
import com.wip.helpdesk_ticketing_system.exception.UserNotFoundException;
import com.wip.helpdesk_ticketing_system.repository.TicketRepository;
import com.wip.helpdesk_ticketing_system.repository.UserRepository;
import com.wip.helpdesk_ticketing_system.sevice.TicketServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TicketServiceImpl Tests")
class TicketServiceImplTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TicketServiceImpl ticketService;

    private User sampleUser;
    private Ticket sampleTicket;
    private TicketDto sampleDto;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setUserId(1L);
        sampleUser.setName("John Doe");
        sampleUser.setEmail("john@example.com");
        sampleUser.setRole(Role.END_USER);
        sampleUser.setPasswordHash("hashed");

        sampleTicket = new Ticket();
        sampleTicket.setTicketId(1L);
        sampleTicket.setTitle("Printer not working");
        sampleTicket.setDescription("The office printer is offline since morning.");
        sampleTicket.setPriority(Priority.HIGH);
        sampleTicket.setStatus(Status.OPEN);
        sampleTicket.setCreatedDate(LocalDateTime.now());
        sampleTicket.setUpdatedDate(LocalDateTime.now());
        sampleTicket.setUser(sampleUser);

        sampleDto = new TicketDto();
        sampleDto.setTitle("Printer not working");
        sampleDto.setDescription("The office printer is offline since morning.");
        sampleDto.setPriority(Priority.HIGH);
        sampleDto.setUserId(1L);
    }

    // ---- createTicket ----

    @Test
    @DisplayName("createTicket: should create ticket with OPEN status when valid DTO is provided")
    void createTicket_ShouldCreateTicketWithOpenStatus() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(sampleTicket);

        Ticket result = ticketService.createTicket(sampleDto);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Printer not working");
        assertThat(result.getStatus()).isEqualTo(Status.OPEN);
        assertThat(result.getUser()).isEqualTo(sampleUser);
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    @DisplayName("createTicket: should throw UserNotFoundException when user does not exist")
    void createTicket_ShouldThrowUserNotFoundException_WhenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        sampleDto.setUserId(99L);

        assertThatThrownBy(() -> ticketService.createTicket(sampleDto))
                .isInstanceOf(UserNotFoundException.class);
        verify(ticketRepository, never()).save(any());
    }

    // ---- getAllTickets ----

    @Test
    @DisplayName("getAllTickets: should return list of all tickets")
    void getAllTickets_ShouldReturnAllTickets() {
        Ticket ticket2 = new Ticket();
        ticket2.setTicketId(2L);
        ticket2.setTitle("Network issue");
        ticket2.setUser(sampleUser);
        when(ticketRepository.findAll()).thenReturn(List.of(sampleTicket, ticket2));

        List<Ticket> result = ticketService.getAllTickets();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("Printer not working");
        assertThat(result.get(1).getTitle()).isEqualTo("Network issue");
    }

    @Test
    @DisplayName("getAllTickets: should return empty list when no tickets exist")
    void getAllTickets_ShouldReturnEmptyList_WhenNoTickets() {
        when(ticketRepository.findAll()).thenReturn(List.of());
        assertThat(ticketService.getAllTickets()).isEmpty();
    }

    // ---- getTicketById ----

    @Test
    @DisplayName("getTicketById: should return ticket when found")
    void getTicketById_ShouldReturnTicket_WhenFound() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(sampleTicket));

        Ticket result = ticketService.getTicketById(1L);

        assertThat(result.getTicketId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Printer not working");
    }

    @Test
    @DisplayName("getTicketById: should throw TicketNotFoundException when not found")
    void getTicketById_ShouldThrowTicketNotFoundException_WhenNotFound() {
        when(ticketRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.getTicketById(999L))
                .isInstanceOf(TicketNotFoundException.class);
    }

    // ---- updateTicket ----

    @Test
    @DisplayName("updateTicket: should update title, description, priority and save")
    void updateTicket_ShouldUpdateFields() {
        TicketDto updateDto = new TicketDto();
        updateDto.setTitle("Printer fixed");
        updateDto.setDescription("Replaced the toner.");
        updateDto.setPriority(Priority.LOW);
        updateDto.setUserId(1L);

        Ticket updated = new Ticket();
        updated.setTicketId(1L);
        updated.setTitle("Printer fixed");
        updated.setDescription("Replaced the toner.");
        updated.setPriority(Priority.LOW);
        updated.setStatus(Status.OPEN);
        updated.setUser(sampleUser);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(sampleTicket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(updated);

        Ticket result = ticketService.updateTicket(1L, updateDto);

        assertThat(result.getTitle()).isEqualTo("Printer fixed");
        assertThat(result.getPriority()).isEqualTo(Priority.LOW);
        verify(ticketRepository).save(any(Ticket.class));
    }

    @Test
    @DisplayName("updateTicket: should throw TicketNotFoundException for missing ticket")
    void updateTicket_ShouldThrow_WhenTicketNotFound() {
        when(ticketRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> ticketService.updateTicket(99L, sampleDto))
                .isInstanceOf(TicketNotFoundException.class);
    }

    // ---- updateStatus ----

    @Test
    @DisplayName("updateStatus: should update ticket status to IN_PROGRESS")
    void updateStatus_ShouldUpdateStatus() {
        Ticket updated = new Ticket();
        updated.setTicketId(1L);
        updated.setStatus(Status.IN_PROGRESS);
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(sampleTicket));
        when(ticketRepository.save(any())).thenReturn(updated);

        Ticket result = ticketService.updateStatus(1L, Status.IN_PROGRESS);

        assertThat(result.getStatus()).isEqualTo(Status.IN_PROGRESS);
        verify(ticketRepository).save(any(Ticket.class));
    }

    // ---- deleteTicket ----

    @Test
    @DisplayName("deleteTicket: should call repository delete when ticket exists")
    void deleteTicket_ShouldCallDelete() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(sampleTicket));

        ticketService.deleteTicket(1L);

        verify(ticketRepository, times(1)).delete(sampleTicket);
    }

    @Test
    @DisplayName("deleteTicket: should throw TicketNotFoundException when ticket missing")
    void deleteTicket_ShouldThrow_WhenNotFound() {
        when(ticketRepository.findById(55L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> ticketService.deleteTicket(55L))
                .isInstanceOf(TicketNotFoundException.class);
    }

    // ---- getTicketsByUser ----

    @Test
    @DisplayName("getTicketsByUser: should return tickets for a given userId")
    void getTicketsByUser_ShouldReturnUserTickets() {
        when(ticketRepository.findByUserUserId(1L)).thenReturn(List.of(sampleTicket));

        List<Ticket> result = ticketService.getTicketsByUser(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUser().getUserId()).isEqualTo(1L);
    }

    // ---- getTicketsByStatus ----

    @Test
    @DisplayName("getTicketsByStatus: should return tickets filtered by OPEN status")
    void getTicketsByStatus_ShouldReturnFilteredTickets() {
        when(ticketRepository.findByStatus(Status.OPEN)).thenReturn(List.of(sampleTicket));

        List<Ticket> result = ticketService.getTicketsByStatus(Status.OPEN);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(Status.OPEN);
    }

    @Test
    @DisplayName("getTicketsByStatus: should return empty list when no tickets match status")
    void getTicketsByStatus_ShouldReturnEmpty_WhenNoneMatch() {
        when(ticketRepository.findByStatus(Status.CLOSED)).thenReturn(List.of());
        assertThat(ticketService.getTicketsByStatus(Status.CLOSED)).isEmpty();
    }
}
