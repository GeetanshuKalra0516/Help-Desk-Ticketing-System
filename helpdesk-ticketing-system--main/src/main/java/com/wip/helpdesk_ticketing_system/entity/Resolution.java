//package com.wip.helpdesk_ticketing_system.entity;
//
//import java.time.LocalDateTime;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//
//import jakarta.persistence.*;
//
//@Entity
//@Table(name = "resolutions")
//public class Resolution {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long resolutionId;
//
//    @OneToOne
//    @JoinColumn(name = "ticket_id")
//    private Ticket ticket;
//
//    @ManyToOne
//    @JoinColumn(name = "resolved_by")
//    private User resolvedBy;
//
//    private String resolutionNotes;
//
//    private LocalDateTime resolvedDate;
//    
//    
//
//    public Resolution() {
//    }
//
//    public Long getResolutionId() {
//        return resolutionId;
//    }
//
//    public void setResolutionId(Long resolutionId) {
//        this.resolutionId = resolutionId;
//    }
//
//    public Ticket getTicket() {
//        return ticket;
//    }
//
//    public void setTicket(Ticket ticket) {
//        this.ticket = ticket;
//    }
//
//    public User getResolvedBy() {
//        return resolvedBy;
//    }
//
//    public void setResolvedBy(User resolvedBy) {
//        this.resolvedBy = resolvedBy;
//    }
//
//    public String getResolutionNotes() {
//        return resolutionNotes;
//    }
//
//    public void setResolutionNotes(String resolutionNotes) {
//        this.resolutionNotes = resolutionNotes;
//    }
//
//    public LocalDateTime getResolvedDate() {
//        return resolvedDate;
//    }
//
//    public void setResolvedDate(LocalDateTime resolvedDate) {
//        this.resolvedDate = resolvedDate;
//    }
//}

package com.wip.helpdesk_ticketing_system.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@Table(name = "resolutions")
public class Resolution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resolutionId;

    // Ticket → avoid User recursion
    @OneToOne
    @JoinColumn(name = "ticket_id")
    @JsonIgnoreProperties({"user"})
    private Ticket ticket;

    // User → avoid tickets recursion
    @ManyToOne
    @JoinColumn(name = "resolved_by")
    @JsonIgnoreProperties({"tickets"})
    private User resolvedBy;

    private String resolutionNotes;

    private LocalDateTime resolvedDate;

    public Resolution() {}

    public Long getResolutionId() {
        return resolutionId;
    }

    public void setResolutionId(Long resolutionId) {
        this.resolutionId = resolutionId;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public User getResolvedBy() {
        return resolvedBy;
    }

    public void setResolvedBy(User resolvedBy) {
        this.resolvedBy = resolvedBy;
    }

    public String getResolutionNotes() {
        return resolutionNotes;
    }

    public void setResolutionNotes(String resolutionNotes) {
        this.resolutionNotes = resolutionNotes;
    }

    public LocalDateTime getResolvedDate() {
        return resolvedDate;
    }

    public void setResolvedDate(LocalDateTime resolvedDate) {
        this.resolvedDate = resolvedDate;
    }
}