package rs.ac.uns.ftn.svtvezbe07.model.dto;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import rs.ac.uns.ftn.svtvezbe07.model.entity.Group;
import rs.ac.uns.ftn.svtvezbe07.model.entity.User;

public class FriendRequestDTO {

    private Long id;

    private boolean approved;

    private LocalDateTime createdAt;
    private LocalDateTime at;

    private User toWho;
  
    private User from;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getAt() {
		return at;
	}

	public void setAt(LocalDateTime at) {
		this.at = at;
	}

	public User getToWho() {
		return toWho;
	}

	public void setToWho(User toWho) {
		this.toWho = toWho;
	}

	public User getFrom() {
		return from;
	}

	public void setFrom(User from) {
		this.from = from;
	}

	public FriendRequestDTO(Long id, boolean approved, LocalDateTime createdAt, LocalDateTime at, User toWho,
			User from) {
		super();
		this.id = id;
		this.approved = approved;
		this.createdAt = createdAt;
		this.at = at;
		this.toWho = toWho;
		this.from = from;
	}

	public FriendRequestDTO() {
	
	}
    
    
}
