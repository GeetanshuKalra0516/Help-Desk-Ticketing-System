package com.wip.helpdesk_ticketing_system.service;

import com.wip.helpdesk_ticketing_system.dto.AssignmentDto;
import com.wip.helpdesk_ticketing_system.dto.ResolutionDto;
import com.wip.helpdesk_ticketing_system.entity.Assignment;
import com.wip.helpdesk_ticketing_system.entity.Resolution;
import com.wip.helpdesk_ticketing_system.entity.Ticket;
import com.wip.helpdesk_ticketing_system.entity.User;
import com.wip.helpdesk_ticketing_system.enums.Priority;
import com.wip.helpdesk_ticketing_system.enums.Role;
import com.wip.helpdesk_ticketing_system.enums.Status;
import com.wip.helpdesk_ticketing_system.exception.AssignmentNotFoundException;
import com.wip.helpdesk_ticketing_system.exception.TicketNotFoundException;
import com.wip.helpdesk_ticketing_system.exception.UserNotFoundException;
import com.wip.helpdesk_ticketing_system.repository.AssignmentRepository;
import com.wip.helpdesk_ticketing_system.repository.ResolutionRepository;
import com.wip.helpdesk_ticketing_system.repository.TicketRepository;
import com.wip.helpdesk_ticketing_system.repository.UserRepository;
import com.wip.helpdesk_ticketing_system.sevice.AssignmentServiceImpl;
import com.wip.helpdesk_ticketing_system.sevice.ResolutionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
@DisplayName("Assignment and Resolution Service Tests")
class AssignmentResolutionServiceTest {

    // ── Shared fixtures ──────────────────────────────────────────────────
    private User endUser;
    private User agent;
    private Ticket openTicket;

    @BeforeEach
    void buildFixtures() {
        endUser = new User();
        endUser.setUserId(1L);
        endUser.setName("End User");
        endUser.setEmail("user@test.com");
        endUser.setRole(Role.END_USER);

        agent = new User();
        agent.setUserId(2L);
        agent.setName("Agent Smith");
        agent.setEmail("agent@test.com");
        agent.setRole(Role.AGENT);

        openTicket = new Ticket();
        openTicket.setTicketId(10L);
        openTicket.setTitle("Keyboard issue");
        openTicket.setDescription("Keys are stuck.");
        openTicket.setPriority(Priority.MEDIUM);
        openTicket.setStatus(Status.OPEN);
        openTicket.setCreatedDate(LocalDateTime.now());
        openTicket.setUpdatedDate(LocalDateTime.now());
        openTicket.setUser(endUser);
    }

    // ════════════════════════════════════════════════════════════════════
    // AssignmentServiceImpl
    // ════════════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("AssignmentServiceImpl")
    class AssignmentServiceTests {

        @Mock private AssignmentRepository assignmentRepository;
        @Mock private TicketRepository ticketRepository;
        @Mock private UserRepository userRepository;
        @InjectMocks private AssignmentServiceImpl assignmentService;

        @Test
        @DisplayName("assignTicket: should create assignment and change ticket status to IN_PROGRESS")
        void assignTicket_ShouldCreateAssignment_AndSetStatusInProgress() {
            AssignmentDto dto = new AssignmentDto();
            dto.setTicketId(10L);
            dto.setAgentId(2L);

            Assignment saved = new Assignment();
            saved.setAssignmentId(1L);
            saved.setTicket(openTicket);
            saved.setAssignedTo(agent);
            saved.setAssignedDate(LocalDateTime.now());

            when(ticketRepository.findById(10L)).thenReturn(Optional.of(openTicket));
            when(userRepository.findById(2L)).thenReturn(Optional.of(agent));
            when(assignmentRepository.save(any(Assignment.class))).thenReturn(saved);
            when(ticketRepository.save(any(Ticket.class))).thenReturn(openTicket);

            Assignment result = assignmentService.assignTicket(dto);

            assertThat(result).isNotNull();
            assertThat(result.getAssignedTo()).isEqualTo(agent);
            // Verify ticket status was updated
            verify(ticketRepository).save(argThat(t -> t.getStatus() == Status.IN_PROGRESS));
        }

        @Test
        @DisplayName("assignTicket: should throw TicketNotFoundException when ticket does not exist")
        void assignTicket_ShouldThrowTicketNotFoundException() {
            AssignmentDto dto = new AssignmentDto();
            dto.setTicketId(999L);
            dto.setAgentId(2L);
            when(ticketRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> assignmentService.assignTicket(dto))
                    .isInstanceOf(TicketNotFoundException.class);
            verify(assignmentRepository, never()).save(any());
        }

        @Test
        @DisplayName("assignTicket: should throw UserNotFoundException when agent does not exist")
        void assignTicket_ShouldThrowUserNotFoundException() {
            AssignmentDto dto = new AssignmentDto();
            dto.setTicketId(10L);
            dto.setAgentId(999L);
            when(ticketRepository.findById(10L)).thenReturn(Optional.of(openTicket));
            when(userRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> assignmentService.assignTicket(dto))
                    .isInstanceOf(UserNotFoundException.class);
        }

        @Test
        @DisplayName("getAllAssignments: should return list of all assignments")
        void getAllAssignments_ShouldReturnList() {
            Assignment a = new Assignment();
            a.setAssignmentId(1L);
            a.setTicket(openTicket);
            a.setAssignedTo(agent);
            when(assignmentRepository.findAll()).thenReturn(List.of(a));

            List<Assignment> result = assignmentService.getAllAssignments();
            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("getAllAssignments: should return empty list when no assignments exist")
        void getAllAssignments_ShouldReturnEmpty() {
            when(assignmentRepository.findAll()).thenReturn(List.of());
            assertThat(assignmentService.getAllAssignments()).isEmpty();
        }

        @Test
        @DisplayName("getAssignmentById: should return assignment when found")
        void getAssignmentById_ShouldReturnAssignment() {
            Assignment a = new Assignment();
            a.setAssignmentId(1L);
            when(assignmentRepository.findById(1L)).thenReturn(Optional.of(a));

            Assignment result = assignmentService.getAssignmentById(1L);
            assertThat(result.getAssignmentId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("getAssignmentById: should throw AssignmentNotFoundException when missing")
        void getAssignmentById_ShouldThrow_WhenNotFound() {
            when(assignmentRepository.findById(99L)).thenReturn(Optional.empty());
            assertThatThrownBy(() -> assignmentService.getAssignmentById(99L))
                    .isInstanceOf(AssignmentNotFoundException.class);
        }
    }

    // ════════════════════════════════════════════════════════════════════
    // ResolutionServiceImpl
    // ════════════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("ResolutionServiceImpl")
    class ResolutionServiceTests {

        @Mock private ResolutionRepository resolutionRepository;
        @Mock private TicketRepository ticketRepository;
        @Mock private UserRepository userRepository;
        @InjectMocks private ResolutionServiceImpl resolutionService;

        @Test
        @DisplayName("resolveTicket: should create resolution and change ticket status to RESOLVED")
        void resolveTicket_ShouldCreateResolution_AndSetStatusResolved() {
            ResolutionDto dto = new ResolutionDto();
            dto.setTicketId(10L);
            dto.setUserId(2L);
            dto.setResolutionNotes("Cleaned the keyboard contacts. Issue fixed.");

            Resolution saved = new Resolution();
            saved.setResolutionId(1L);
            saved.setTicket(openTicket);
            saved.setResolvedBy(agent);
            saved.setResolutionNotes(dto.getResolutionNotes());
            saved.setResolvedDate(LocalDateTime.now());

            when(ticketRepository.findById(10L)).thenReturn(Optional.of(openTicket));
            when(userRepository.findById(2L)).thenReturn(Optional.of(agent));
            when(resolutionRepository.save(any(Resolution.class))).thenReturn(saved);
            when(ticketRepository.save(any(Ticket.class))).thenReturn(openTicket);

            Resolution result = resolutionService.resolveTicket(dto);

            assertThat(result).isNotNull();
            assertThat(result.getResolutionNotes()).isEqualTo("Cleaned the keyboard contacts. Issue fixed.");
            assertThat(result.getResolvedBy()).isEqualTo(agent);
            // Verify ticket status was updated to RESOLVED
            verify(ticketRepository).save(argThat(t -> t.getStatus() == Status.RESOLVED));
        }

        @Test
        @DisplayName("resolveTicket: should throw TicketNotFoundException when ticket missing")
        void resolveTicket_ShouldThrowTicketNotFoundException() {
            ResolutionDto dto = new ResolutionDto();
            dto.setTicketId(777L);
            dto.setUserId(2L);
            when(ticketRepository.findById(777L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> resolutionService.resolveTicket(dto))
                    .isInstanceOf(TicketNotFoundException.class);
            verify(resolutionRepository, never()).save(any());
        }

        @Test
        @DisplayName("resolveTicket: should throw UserNotFoundException when resolver missing")
        void resolveTicket_ShouldThrowUserNotFoundException() {
            ResolutionDto dto = new ResolutionDto();
            dto.setTicketId(10L);
            dto.setUserId(888L);
            when(ticketRepository.findById(10L)).thenReturn(Optional.of(openTicket));
            when(userRepository.findById(888L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> resolutionService.resolveTicket(dto))
                    .isInstanceOf(UserNotFoundException.class);
        }

        @Test
        @DisplayName("getAllResolutions: should return all resolutions")
        void getAllResolutions_ShouldReturnList() {
            Resolution r = new Resolution();
            r.setResolutionId(1L);
            when(resolutionRepository.findAll()).thenReturn(List.of(r));

            assertThat(resolutionService.getAllResolutions()).hasSize(1);
        }

        @Test
        @DisplayName("getAllResolutions: should return empty list when none exist")
        void getAllResolutions_ShouldReturnEmpty() {
            when(resolutionRepository.findAll()).thenReturn(List.of());
            assertThat(resolutionService.getAllResolutions()).isEmpty();
        }
    }
}
