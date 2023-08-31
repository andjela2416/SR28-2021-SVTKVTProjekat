package rs.ac.uns.ftn.svtvezbe07.model.entity;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "posts")
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "content", nullable = false)
	private String content;

	@Column(name = "creation_date")
	private LocalDateTime creationDate;
	
	@Column(columnDefinition = "BOOL",name="isdeleted")
	private boolean isDeleted;
	
	@Column(columnDefinition = "BOOL",name="comments_for_post")
	private boolean commentsForPost;
	
	public boolean isCommentsForPost() {
		return commentsForPost;
	}

	public void setCommentsForPost(boolean commentsForPost) {
		this.commentsForPost = commentsForPost;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	/*@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name="id_slike")
	private Set<Image> slike;
	*/
	@JsonIgnoreProperties("post")
	@OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(
        name = "post_images",
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name = "image_id")
    )
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Set<Image> images = new HashSet<>();
	
	@JsonIgnoreProperties("post")
	@OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(
        name = "post_comments",
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name = "comment_id")
    )
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Set<Comment> comments = new HashSet<>();
	
	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	@JsonIgnoreProperties({"friends", "lastName", "firstName","description","password","displayName","lastLogin","role","email"})
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="user_id")
	private User postedBy;

//	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
//	private Set<Reaction> reactions = new HashSet<>();
	@JsonIgnoreProperties({"id","timestamp","comment","post"})
	@OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(
        name = "post_reactions",
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name = "reaction_id")
    )
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Set<Reaction> reactions = new HashSet<>();
	
	@Column(name = "likes")
	private int likes;
	
	@Column(name = "dislikes")
	private int dislikes;
	
	@Column(name = "hearts")
	private int hearts;
	
	

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public int getDislikes() {
		return dislikes;
	}

	public void setDislikes(int dislikes) {
		this.dislikes = dislikes;
	}

	public int getHearts() {
		return hearts;
	}

	public void setHearts(int hearts) {
		this.hearts = hearts;
	}

	@OnDelete(action = OnDeleteAction.CASCADE) 
	@JsonIgnoreProperties("posts")
	@JoinColumn(name="group_id")
	@ManyToOne(fetch = FetchType.EAGER)
	private Group group;
	
	public Long getId() {
		return id;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public User getPostedBy() {
		return postedBy;
	}

	public void setPostedBy(User postedBy) {
		this.postedBy = postedBy;
	}

	public Set<Image> getImages() {
		return images;
	}

	public void setImages(Set<Image> images) {
		this.images = images;
	}

	public Set<Reaction> getReactions() {
		return reactions;
	}

	public void setReactions(Set<Reaction> reactions) {
		this.reactions = reactions;
	}

	

	public Post() {
		super();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Post t = (Post) o;
		return id != null && id.equals(t.getId());
	}

	@Override
	public int hashCode() {
		/*
		 * Pretpostavka je da je u pitanju tranzijentni objekat (jos nije sacuvan u bazu) i da id ima null vrednost.
		 * Kada se sacuva u bazu dobice non-null vrednost. To znaci da ce objekat imati razlicite kljuceve u dva stanja, te ce za generisan
		 * hashCode i equals vratiti razlicite vrednosti. Vracanje konstantne vrednosti resava ovaj problem.
		 * Sa druge strane ovakva implementacija moze da afektuje performanse u slucaju velikog broja objekata
		 * koji ce zavrsiti u istom hash bucket-u.
		 */
		return 1337;
	}

}
