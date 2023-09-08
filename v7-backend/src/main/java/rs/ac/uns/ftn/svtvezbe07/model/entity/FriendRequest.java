package rs.ac.uns.ftn.svtvezbe07.model.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table
public class FriendRequest {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "approved")
    private boolean approved;

    @Column(name = "created")
    private LocalDateTime createdAt;

    @Column(name = "at")
    private LocalDateTime at;
  
    @OneToOne
    @JoinColumn(name = "fromWho")
    private User fromWho;

    @OneToOne
    @JoinColumn(name = "toWho")
    private User toWho;
    
    @Column(columnDefinition = "BOOL",name="isdeleted")
	private boolean isDeleted;

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

	public User getFromWho() {
		return fromWho;
	}

	public void setFromWho(User from) {
		this.fromWho = from;
	}

	public User getToWho() {
		return toWho;
	}

	public void setToWho(User toWho) {
		this.toWho = toWho;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
    
}
