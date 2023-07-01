package rs.ac.uns.ftn.svtvezbe07.model.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name = "comments")
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String text;

	@Column(name="_timestamp")
	private LocalDateTime timestamp;

	@Column(columnDefinition = "BOOL",name="isdeleted")
	private boolean isDeleted;

	@JsonIgnoreProperties({"username","friends", "lastName", "firstName","description","password","displayName","lastLogin","role","email"})
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="user_id")
	private User userId;

	@JsonIgnoreProperties({"content","creationDate","images","postedBy"})
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="post_id")
	private Post post;

	@OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "comment_replies",
        joinColumns = @JoinColumn(name = "comment_id"),
        inverseJoinColumns = @JoinColumn(name = "reply_id")
    )
    private Set<Comment> repliesComment = new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}


	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public User getUserId() {
		return userId;
	}

	public void setUserId(User user) {
		this.userId = user;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	
	public Set<Comment> getRepliesComment() {
		return repliesComment;
	}

	public void setRepliesComment(Set<Comment> repliesComment) {
		this.repliesComment = repliesComment;
	}

	public Comment() {

	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Comment t = (Comment) o;
		return id != null && id.equals(t.getId());
	}

	@Override
	public int hashCode() {
		return 1337;
	}
}
