package com.wip.helpdesk_ticketing_system.dto;

public class AssignmentDto {

    private Long ticketId;
    private Long assignedTo;

    public AssignmentDto() {
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public Long getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Long assignedTo) {
        this.assignedTo = assignedTo;
    }
}