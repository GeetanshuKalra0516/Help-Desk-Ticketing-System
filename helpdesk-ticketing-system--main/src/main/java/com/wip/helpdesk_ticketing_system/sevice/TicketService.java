package com.wip.helpdesk_ticketing_system.sevice;

import java.util.List;

import com.wip.helpdesk_ticketing_system.dto.TicketDto;
import com.wip.helpdesk_ticketing_system.entity.Ticket;

public interface TicketService {

    Ticket createTicket(TicketDto dto);

    List<Ticket> getAllTickets();

    Ticket getTicketById(Long id);

    Ticket updateTicket(Long id, Ticket ticket);

    void deleteTicket(Long id);
}