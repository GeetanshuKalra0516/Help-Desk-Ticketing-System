package com.wip.helpdesk_ticketing_system.sevice;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wip.helpdesk_ticketing_system.entity.Assignment;
import com.wip.helpdesk_ticketing_system.entity.Ticket;
import com.wip.helpdesk_ticketing_system.entity.User;
import com.wip.helpdesk_ticketing_system.enums.Status;
import com.wip.helpdesk_ticketing_system.exception.TicketNotFoundException;
import com.wip.helpdesk_ticketing_system.exception.UserNotFoundException;
import com.wip.helpdesk_ticketing_system.repository.AssignmentRepository;
import com.wip.helpdesk_ticketing_system.repository.TicketRepository;
import com.wip.helpdesk_ticketing_system.repository.UserRepository;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Assignment assignTicket(Long ticketId, Long agentId) {

        // Fetch Ticket
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));

        // Fetch Agent
        User agent = userRepository.findById(agentId)
                .orElseThrow(() -> new UserNotFoundException(agentId));

        // Create Assignment
        Assignment assignment = new Assignment();
        assignment.setTicket(ticket);
        assignment.setAssignedTo(agent);
        assignment.setAssignedDate(LocalDateTime.now());

        
        ticket.setStatus(Status.IN_PROGRESS);
        ticketRepository.save(ticket);

        return assignmentRepository.save(assignment);
    }

    @Override
    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }
}