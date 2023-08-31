package rs.ac.uns.ftn.svtvezbe07.model.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import rs.ac.uns.ftn.svtvezbe07.model.entity.Group;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Image;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Post;
import rs.ac.uns.ftn.svtvezbe07.model.entity.User;

public class GroupDTO {
    private int id;

    private String name;
    
    private String description;
    
	private boolean isDeleted;

    private LocalDateTime creationDate;

	private Long groupAdmin;

    private Set<Post> posts = new HashSet<>();

    private boolean suspended;
    
    private String suspendedReason;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public Long getGroupAdmin() {
		return groupAdmin;
	}

	public void setGroupAdmin(Long groupAdmin) {
		this.groupAdmin = groupAdmin;
	}

	public Set<Post> getPosts() {
		return posts;
	}

	public void setPosts(Set<Post> posts) {
		this.posts = posts;
	}

	public boolean isSuspended() {
		return suspended;
	}

	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}

	public String getSuspendedReason() {
		return suspendedReason;
	}

	public void setSuspendedReason(String suspendedReason) {
		this.suspendedReason = suspendedReason;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public GroupDTO(int id, String name,boolean deleted, String description, LocalDateTime creationDate, Long groupAdmin,
			Set<Post> posts, boolean suspended, String suspendedReason) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.creationDate = creationDate;
		this.groupAdmin = groupAdmin;
		this.isDeleted=deleted;
		this.posts = posts;
		this.suspended = suspended;
		this.suspendedReason = suspendedReason;
	}

	public GroupDTO() {
		super();
	}
    


	public GroupDTO(Group g) {
		this(g.getGroupAdmin().getId(),g.getName(),g.isDeleted(),g.getDescription(),g.getCreationDate(),g.getId(),g.getPosts(),g.isSuspended(),g.getSuspendedReason());
	}
    
}
