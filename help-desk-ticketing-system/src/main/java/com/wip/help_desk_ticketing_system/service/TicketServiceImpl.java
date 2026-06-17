package com.wip.help_desk_ticketing_system.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.wip.help_desk_ticketing_system.entity.Assignment;
import com.wip.help_desk_ticketing_system.entity.Resolution;
import com.wip.help_desk_ticketing_system.entity.Ticket;
import com.wip.help_desk_ticketing_system.entity.User;
import com.wip.help_desk_ticketing_system.repository.AssignmentRepository;
import com.wip.help_desk_ticketing_system.repository.ResolutionRepository;
import com.wip.help_desk_ticketing_system.repository.TicketRepository;
import com.wip.help_desk_ticketing_system.repository.UserRepository;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepo;
    private final UserRepository userRepo;
    private final AssignmentRepository assignRepo;
    private final ResolutionRepository resRepo;

    public TicketServiceImpl(TicketRepository ticketRepo, UserRepository userRepo,
                             AssignmentRepository assignRepo, ResolutionRepository resRepo) {
        this.ticketRepo = ticketRepo;
        this.userRepo = userRepo;
        this.assignRepo = assignRepo;
        this.resRepo = resRepo;
    }

    @Override
    public Ticket createTicket(Ticket ticket) {
        ticket.setStatus("Open");
        ticket.setCreatedDate(LocalDateTime.now());
        User existingUser = userRepo.findById(ticket.getUser().getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        ticket.setUser(existingUser);
        return ticketRepo.save(ticket);
    }

    @Override
    public List<Ticket> getAllTickets() {
        return ticketRepo.findAll();
    }

    @Override
    public Ticket assignTicket(Long ticketId, Long userId) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        Optional<User> userOpt = userRepo.findById(userId);

        if (ticketOpt.isEmpty() || userOpt.isEmpty()) {
            throw new RuntimeException("Ticket or User not found");
        }

        Ticket ticket = ticketOpt.get();
        User user = userOpt.get();

        Assignment assign = new Assignment(null, ticket, user, LocalDateTime.now());
        assignRepo.save(assign);

        ticket.setStatus("In Progress");
        return ticketRepo.save(ticket);
    }

    @Override
    public Ticket resolveTicket(Long ticketId, String notes, Long userId) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        Optional<User> userOpt = userRepo.findById(userId);

        if (ticketOpt.isEmpty() || userOpt.isEmpty()) {
            throw new RuntimeException("Ticket or User not found");
        }

        Ticket ticket = ticketOpt.get();
        User user = userOpt.get();

        Resolution res = new Resolution(null, ticket, user, notes, LocalDateTime.now());
        resRepo.save(res);

        ticket.setStatus("Resolved");
        return ticketRepo.save(ticket);
    }
}
