package com.wip.helpdesk_ticketing_system.sevice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.wip.helpdesk_ticketing_system.dto.TicketDto;
import com.wip.helpdesk_ticketing_system.entity.Ticket;
import com.wip.helpdesk_ticketing_system.sevice.TicketService;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping
    public Ticket createTicket(@RequestBody TicketDto dto) {
        return ticketService.createTicket(dto);
    }

    @GetMapping
    public List<Ticket> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @GetMapping("/{id}")
    public Ticket getTicketById(@PathVariable Long id) {
        return ticketService.getTicketById(id);
    }
}