package com.wip.help_desk_ticketing_system.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wip.help_desk_ticketing_system.entity.Ticket;
import com.wip.help_desk_ticketing_system.service.TicketService;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket) {
        return ResponseEntity.ok(ticketService.createTicket(ticket));
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @PutMapping("/{ticketId}/assign/{userId}")
    public ResponseEntity<Ticket> assignTicket(@PathVariable Long ticketId, @PathVariable Long userId) {
        return ResponseEntity.ok(ticketService.assignTicket(ticketId, userId));
    }

    @PutMapping("/{ticketId}/resolve/{userId}")
    public ResponseEntity<Ticket> resolveTicket(@PathVariable Long ticketId,
                                                @PathVariable Long userId,
                                                @RequestBody String notes) {
        return ResponseEntity.ok(ticketService.resolveTicket(ticketId, notes, userId));
    }
}
