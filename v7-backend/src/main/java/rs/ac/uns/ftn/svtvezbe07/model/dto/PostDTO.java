package rs.ac.uns.ftn.svtvezbe07.model.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Group;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Image;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Post;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Reaction;
import rs.ac.uns.ftn.svtvezbe07.model.entity.User;

@Getter
@Setter
public class PostDTO {

	private Long id;
	private String content;
	private LocalDateTime creationDate;
	private Set<Image> images;
	private User postedBy;
	private Set<Reaction> reactions = new HashSet<Reaction>();
	private boolean isDeleted;
	private Group group;
    public PostDTO() {
        
    }
	

	public Group getGroup() {
		return group;
	}


	public void setGroup(Group group) {
		this.group = group;
	}


	public boolean isDeleted() {
		return isDeleted;
	}


	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}


	public Set<Reaction> getReactions() {
		return reactions;
	}



	public void setReactions(Set<Reaction> reactions) {
		this.reactions = reactions;
	}



	public Set<Image> getImages() {
		return images;
	}


	public void setImages(Set<Image> images) {
		this.images = images;
	}


	public PostDTO(Post post) {
		this(post.getId(),post.getContent(),post.getCreationDate(),post.getPostedBy(),post.getImages(),post.getGroup());
	}
	public PostDTO(Long id, String content, LocalDateTime creationDate, User postedBy, Set<Image> set,Group g) {
		super();
		this.id = id;
		this.content = content;
		this.creationDate = creationDate;
		this.postedBy = postedBy;
		this.images = set;
		this.group=g;
	}
    private static Set<ImageDTO> convertImagesToDTOs(Set<Image> images) {
        Set<ImageDTO> imageDTOs = new HashSet<>();
        for (Image image : images) {
            ImageDTO imageDTO = new ImageDTO(image);
            imageDTOs.add(imageDTO);
        }
        return imageDTOs;
    }
	
	public Long getId() {
		return id;
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

	
	
}
