package com.wip.help_desk_ticketing_system.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assignmentId;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    private LocalDateTime assignedDate = LocalDateTime.now();

	public Assignment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Assignment(Long assignmentId, Ticket ticket, User assignedTo, LocalDateTime assignedDate) {
		super();
		this.assignmentId = assignmentId;
		this.ticket = ticket;
		this.assignedTo = assignedTo;
		this.assignedDate = assignedDate;
	}

	public Long getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(Long assignmentId) {
		this.assignmentId = assignmentId;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public User getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(User assignedTo) {
		this.assignedTo = assignedTo;
	}

	public LocalDateTime getAssignedDate() {
		return assignedDate;
	}

	public void setAssignedDate(LocalDateTime assignedDate) {
		this.assignedDate = assignedDate;
	}

	@Override
	public String toString() {
		return "Assignment [assignmentId=" + assignmentId + ", ticket=" + ticket + ", assignedTo=" + assignedTo
				+ ", assignedDate=" + assignedDate + "]";
	}
    
    
    
}
