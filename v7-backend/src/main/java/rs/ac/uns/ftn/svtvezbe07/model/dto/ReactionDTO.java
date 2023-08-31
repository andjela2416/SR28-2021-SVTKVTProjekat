package rs.ac.uns.ftn.svtvezbe07.model.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import rs.ac.uns.ftn.svtvezbe07.model.entity.Comment;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Post;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Reaction;
import rs.ac.uns.ftn.svtvezbe07.model.entity.ReactionType;
import rs.ac.uns.ftn.svtvezbe07.model.entity.User;

public class ReactionDTO {
    private Long id;

    private ReactionType type;

    private LocalDateTime timestamp;
    
    private int user;

    private Long comment;

    private Long post;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ReactionType getType() {
		return type;
	}

	public void setType(ReactionType type) {
		this.type = type;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}




	public int getUser() {
		return user;
	}

	public void setUser(int user) {
		this.user = user;
	}

	public Long getComment() {
		return comment;
	}

	public void setComment(Long comment) {
		this.comment = comment;
	}

	public Long getPost() {
		return post;
	}

	public void setPost(Long post) {
		this.post = post;
	}

	public ReactionDTO(Long id, ReactionType type, LocalDateTime timestamp,Long post, int user, Long comment) {
		super();
		this.id = id;
		this.type = type;
		this.timestamp = timestamp;
		this.user = user;
		this.comment = comment;
		this.post = post;
	}

	public ReactionDTO() {

	}

	public ReactionDTO(Reaction post) {

		this(post.getId(),post.getType(),post.getTimestamp(),post.getComment().getId(),post.getUser().getId(),post.getPost().getId());
	}
    
}
