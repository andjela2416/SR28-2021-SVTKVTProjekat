package rs.ac.uns.ftn.svtvezbe07.model.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Comment;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Post;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Reaction;
import rs.ac.uns.ftn.svtvezbe07.model.entity.User;
@Getter
@Setter
public class CommentDTO {

	private Long id;

	private String text;

	private LocalDateTime timeStamp;

	private boolean isDeleted;

	private User user;

	private Long post;

	private Set<Comment> replies = new HashSet<Comment>();
	
	private Set<Reaction> reactions = new HashSet<Reaction>();

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

	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	

	public Long getPost() {
		return post;
	}

	public void setPost(Long post) {
		this.post = post;
	}

	public Set<Comment> getReplies() {
		return replies;
	}

	public void setReplies(Set<Comment> replies) {
		this.replies = replies;
	}


	public CommentDTO(Long id, String text, LocalDateTime timeStamp, boolean isDeleted, User user, Long post,
			Set<Comment> replies) {
		super();
		this.id = id;
		this.text = text;
		this.timeStamp = timeStamp;
		this.isDeleted = isDeleted;
		this.user = user;
		this.post = post;
		this.replies = replies;
	}

	public CommentDTO() {

	}

	public CommentDTO(Comment post) {

		this(post.getId(),post.getText(),post.getTimestamp(),post.isDeleted(),post.getUserId(),post.getPost().getId(),post.getRepliesComment());
	}

}
