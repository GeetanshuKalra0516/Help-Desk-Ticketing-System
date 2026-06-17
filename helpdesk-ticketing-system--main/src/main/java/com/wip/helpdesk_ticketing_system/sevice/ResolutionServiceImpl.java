package com.wip.helpdesk_ticketing_system.sevice;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wip.helpdesk_ticketing_system.entity.Resolution;
import com.wip.helpdesk_ticketing_system.entity.Ticket;
import com.wip.helpdesk_ticketing_system.entity.User;
import com.wip.helpdesk_ticketing_system.enums.Status;
import com.wip.helpdesk_ticketing_system.exception.TicketNotFoundException;
import com.wip.helpdesk_ticketing_system.exception.UserNotFoundException;
import com.wip.helpdesk_ticketing_system.repository.ResolutionRepository;
import com.wip.helpdesk_ticketing_system.repository.TicketRepository;
import com.wip.helpdesk_ticketing_system.repository.UserRepository;

@Service
public class ResolutionServiceImpl implements ResolutionService {


@Autowired
private ResolutionRepository resolutionRepository;

@Autowired
private TicketRepository ticketRepository;

@Autowired
private UserRepository userRepository;

@Override
public Resolution resolveTicket(Long ticketId, Long userId, String notes) {

    // Fetch Ticket
    Ticket ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new TicketNotFoundException(ticketId));

    // Fetch User (Agent/Admin who resolves)
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

    // Create Resolution
    Resolution resolution = new Resolution();
    resolution.setTicket(ticket);
    resolution.setResolvedBy(user);
    resolution.setResolutionNotes(notes);
    resolution.setResolvedDate(LocalDateTime.now());

    // Update Ticket Status
    ticket.setStatus(Status.RESOLVED);
    ticketRepository.save(ticket);

    return resolutionRepository.save(resolution);
}

@Override
public List<Resolution> getAllResolutions() {
    return resolutionRepository.findAll();
}


}
