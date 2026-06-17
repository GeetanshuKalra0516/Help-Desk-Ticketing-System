package com.wip.help_desk_ticketing_system.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Resolution {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resolutionId;

    @OneToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "resolved_by")
    private User resolvedBy;

    private String resolutionNotes;
    private LocalDateTime resolvedDate = LocalDateTime.now();
	public Resolution() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Resolution(Long resolutionId, Ticket ticket, User resolvedBy, String resolutionNotes,
			LocalDateTime resolvedDate) {
		super();
		this.resolutionId = resolutionId;
		this.ticket = ticket;
		this.resolvedBy = resolvedBy;
		this.resolutionNotes = resolutionNotes;
		this.resolvedDate = resolvedDate;
	}
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
	@Override
	public String toString() {
		return "Resolution [resolutionId=" + resolutionId + ", ticket=" + ticket + ", resolvedBy=" + resolvedBy
				+ ", resolutionNotes=" + resolutionNotes + ", resolvedDate=" + resolvedDate + "]";
	}
    
    
}
