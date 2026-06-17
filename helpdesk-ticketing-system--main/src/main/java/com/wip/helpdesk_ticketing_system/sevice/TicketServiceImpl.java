package com.wip.helpdesk_ticketing_system.sevice;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wip.helpdesk_ticketing_system.dto.TicketDto;
import com.wip.helpdesk_ticketing_system.entity.Ticket;
import com.wip.helpdesk_ticketing_system.entity.User;
import com.wip.helpdesk_ticketing_system.enums.Status;
import com.wip.helpdesk_ticketing_system.exception.TicketNotFoundException;
import com.wip.helpdesk_ticketing_system.exception.UserNotFoundException;
import com.wip.helpdesk_ticketing_system.repository.TicketRepository;
import com.wip.helpdesk_ticketing_system.repository.UserRepository;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Ticket createTicket(TicketDto dto) {

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() ->
                        new UserNotFoundException(dto.getUserId()));

        Ticket ticket = new Ticket();

        ticket.setTitle(dto.getTitle());
        ticket.setDescription(dto.getDescription());
        ticket.setPriority(dto.getPriority());
        ticket.setStatus(Status.OPEN);
        ticket.setCreatedDate(LocalDateTime.now());

        // Set User
        ticket.setUser(user);

        return ticketRepository.save(ticket);
    }

    @Override
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Override
    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() ->
                        new TicketNotFoundException(id));
    }

    @Override
    public Ticket updateTicket(Long id, Ticket ticket) {

        Ticket existing = getTicketById(id);

        existing.setTitle(ticket.getTitle());
        existing.setDescription(ticket.getDescription());
        existing.setPriority(ticket.getPriority());
        existing.setStatus(ticket.getStatus());

        return ticketRepository.save(existing);
    }

    @Override
    public void deleteTicket(Long id) {
        ticketRepository.delete(getTicketById(id));
    }
}
