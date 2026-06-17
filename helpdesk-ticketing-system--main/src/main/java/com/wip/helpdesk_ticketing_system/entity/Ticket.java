//package com.wip.helpdesk_ticketing_system.entity;
//
//import java.time.LocalDateTime;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.wip.helpdesk_ticketing_system.enums.Priority;
//import com.wip.helpdesk_ticketing_system.enums.Status;
//
//import jakarta.persistence.*;
//
//@Entity
//@Table(name = "tickets")
//public class Ticket {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long ticketId;
//
//    private String title;
//
//    private String description;
//
//    private LocalDateTime createdDate;
//
//    @Enumerated(EnumType.STRING)
//    private Priority priority;
//
//    @Enumerated(EnumType.STRING)
//    private Status status;
//
////    @ManyToOne
////    @JoinColumn(name = "user_id")
////    private User user;
//    
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    @JsonIgnoreProperties("tickets")
//    private User user;
//
//    public Ticket() {
//    }
//
//	public Long getTicketId() {
//		return ticketId;
//	}
//
//	public void setTicketId(Long ticketId) {
//		this.ticketId = ticketId;
//	}
//
//	public String getTitle() {
//		return title;
//	}
//
//	public void setTitle(String title) {
//		this.title = title;
//	}
//
//	public String getDescription() {
//		return description;
//	}
//
//	public void setDescription(String description) {
//		this.description = description;
//	}
//
//	public LocalDateTime getCreatedDate() {
//		return createdDate;
//	}
//
//	public void setCreatedDate(LocalDateTime createdDate) {
//		this.createdDate = createdDate;
//	}
//
//	public Priority getPriority() {
//		return priority;
//	}
//
//	public void setPriority(Priority priority) {
//		this.priority = priority;
//	}
//
//	public Status getStatus() {
//		return status;
//	}
//
//	public void setStatus(Status status) {
//		this.status = status;
//	}
//
//	public User getUser() {
//		return user;
//	}
//
//	public void setUser(User user) {
//		this.user = user;
//	}
//
//    
//}

package com.wip.helpdesk_ticketing_system.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wip.helpdesk_ticketing_system.enums.Priority;
import com.wip.helpdesk_ticketing_system.enums.Status;

import jakarta.persistence.*;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;

    private String title;

    private String description;

    private LocalDateTime createdDate;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private Status status;

    // ✅ USER LINK (correct)
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("tickets")
    private User user;

    // 🚨 IMPORTANT FIX FOR SWAGGER (add this)
    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL)
    @JsonIgnore
    private Assignment assignment;

    // 🚨 IMPORTANT FIX FOR SWAGGER (add this)
    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL)
    @JsonIgnore
    private Resolution resolution;

    public Ticket() {}

    // getters & setters
    public Long getTicketId() { return ticketId; }
    public void setTicketId(Long ticketId) { this.ticketId = ticketId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Assignment getAssignment() { return assignment; }
    public void setAssignment(Assignment assignment) { this.assignment = assignment; }

    public Resolution getResolution() { return resolution; }
    public void setResolution(Resolution resolution) { this.resolution = resolution; }
}