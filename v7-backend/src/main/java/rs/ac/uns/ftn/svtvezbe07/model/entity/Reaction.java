package rs.ac.uns.ftn.svtvezbe07.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "reactions")
public class Reaction {
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(nullable = false,name="type_")
	    @Enumerated(EnumType.STRING)
	    private ReactionType type;
	    
	    @Column(columnDefinition = "BOOL",name="isdeleted")
		private boolean isDeleted;
	    
	    public boolean isDeleted() {
			return isDeleted;
		}

		public void setDeleted(boolean isDeleted) {
			this.isDeleted = isDeleted;
		}

		@Column(name="timestamp_")
	    private LocalDateTime timestamp;
	    
		@JsonIgnoreProperties({"username","friends", "lastName", "firstName","description","password","displayName","lastLogin","role","email"})
	    @ManyToOne
	    @JoinColumn(name = "user_id")
	    private User user;
	
		@OnDelete(action = OnDeleteAction.CASCADE) 
		@JsonIgnoreProperties({"reactions","replies","userId","post"})
		@JoinColumn(name="comment_id")
		@ManyToOne(fetch = FetchType.EAGER)
		private Comment comment;
		
	
//	    @ManyToOne
//	    @JoinColumn(name = "post_id")
//	    private Post post;
		@OnDelete(action = OnDeleteAction.CASCADE) 
		@JsonIgnoreProperties({"content","creationDate","images","postedBy","reactions"})
		@JoinColumn(name="post_id")
		@ManyToOne(fetch = FetchType.EAGER)
		private Post post;
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

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public Comment getComment() {
			return comment;
		}

		public void setComment(Comment comment) {
			this.comment = comment;
		}

		public Post getPost() {
			return post;
		}

		public void setPost(Post post) {
			this.post = post;
		}

		public Reaction() {
		}

	    
	    
}
