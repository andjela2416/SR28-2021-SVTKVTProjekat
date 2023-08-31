package rs.ac.uns.ftn.svtvezbe07.model.dto;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import rs.ac.uns.ftn.svtvezbe07.model.entity.Group;
import rs.ac.uns.ftn.svtvezbe07.model.entity.User;

public class GroupRequestDTO {

    private Long id;

    private boolean approved;

    private LocalDateTime createdAt;
    private LocalDateTime at;

    private User user;
  
    private Group group;

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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public GroupRequestDTO(Long id, boolean approved, LocalDateTime createdAt, LocalDateTime at, User user,
			Group group) {
		super();
		this.id = id;
		this.approved = approved;
		this.createdAt = createdAt;
		this.at = at;
		this.user = user;
		this.group = group;
	}

	public GroupRequestDTO() {
	
	}
    
    
}
