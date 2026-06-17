package com.wip.helpdesk_ticketing_system.sevice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.wip.helpdesk_ticketing_system.entity.Resolution;
import com.wip.helpdesk_ticketing_system.sevice.ResolutionService;

@RestController
@RequestMapping("/resolutions")
public class ResolutionController {


@Autowired
private ResolutionService resolutionService;

// Resolve Ticket
@PostMapping("/{ticketId}/{userId}")
public Resolution resolveTicket(
        @PathVariable Long ticketId,
        @PathVariable Long userId,
        @RequestParam String notes) {

    return resolutionService.resolveTicket(ticketId, userId, notes);
}

// Get All Resolutions
@GetMapping
public List<Resolution> getAllResolutions() {
    return resolutionService.getAllResolutions();
}


}
