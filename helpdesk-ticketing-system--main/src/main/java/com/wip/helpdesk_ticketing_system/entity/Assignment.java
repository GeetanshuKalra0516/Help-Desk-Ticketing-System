//package com.wip.helpdesk_ticketing_system.entity;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import jakarta.persistence.*;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "assignments")
//public class Assignment {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long assignmentId;
//
//    @OneToOne
//    @JoinColumn(name = "ticket_id")
//    @JsonIgnoreProperties({"user"})
//    private Ticket ticket;
//
//    @ManyToOne
//    @JoinColumn(name = "assigned_to")
//    @JsonIgnoreProperties({"tickets"})
//    private User assignedTo;
//
//    private LocalDateTime assignedDate;
//
//    public Assignment() {}
//
//	public Long getAssignmentId() {
//		return assignmentId;
//	}
//
//	public void setAssignmentId(Long assignmentId) {
//		this.assignmentId = assignmentId;
//	}
//
//	public Ticket getTicket() {
//		return ticket;
//	}
//
//	public void setTicket(Ticket ticket) {
//		this.ticket = ticket;
//	}
//
//	public User getAssignedTo() {
//		return assignedTo;
//	}
//
//	public void setAssignedTo(User assignedTo) {
//		this.assignedTo = assignedTo;
//	}
//
//	public LocalDateTime getAssignedDate() {
//		return assignedDate;
//	}
//
//	public void setAssignedDate(LocalDateTime assignedDate) {
//		this.assignedDate = assignedDate;
//	}
//  
//}

package com.wip.helpdesk_ticketing_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "assignments")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assignmentId;

    // Ticket → avoid user recursion
    @OneToOne
    @JoinColumn(name = "ticket_id")
    @JsonIgnoreProperties({"user"})
    private Ticket ticket;

    // User → avoid tickets recursion (correct)
    @ManyToOne
    @JoinColumn(name = "assigned_to")
    @JsonIgnoreProperties({"tickets"})
    private User assignedTo;

    private LocalDateTime assignedDate;

    public Assignment() {}

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
}