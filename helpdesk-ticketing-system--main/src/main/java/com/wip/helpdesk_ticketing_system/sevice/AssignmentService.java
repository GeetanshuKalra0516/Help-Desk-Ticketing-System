package com.wip.helpdesk_ticketing_system.sevice;

import java.util.List;

import com.wip.helpdesk_ticketing_system.entity.Assignment;

public interface AssignmentService {

    Assignment assignTicket(Long ticketId, Long agentId);

    List<Assignment> getAllAssignments();
}