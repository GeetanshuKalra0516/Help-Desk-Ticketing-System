package com.wip.help_desk_ticketing_system.service;

import java.util.List;

import com.wip.help_desk_ticketing_system.entity.Ticket;

public interface TicketService {
    Ticket createTicket(Ticket ticket);
    List<Ticket> getAllTickets();
    Ticket assignTicket(Long ticketId, Long userId);
    Ticket resolveTicket(Long ticketId, String notes, Long userId);
}
