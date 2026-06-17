package com.wip.helpdesk_ticketing_system.sevice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.wip.helpdesk_ticketing_system.entity.Assignment;
import com.wip.helpdesk_ticketing_system.sevice.AssignmentService;

@RestController
@RequestMapping("/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    // Assign Ticket to Agent
    @PostMapping("/{ticketId}/{agentId}")
    public Assignment assignTicket(
            @PathVariable Long ticketId,
            @PathVariable Long agentId) {

        return assignmentService.assignTicket(ticketId, agentId);
    }

    // Get All Assignments
    @GetMapping
    public List<Assignment> getAllAssignments() {
        return assignmentService.getAllAssignments();
    }
}