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
import rs.ac.uns.ftn.svtvezbe07.model.entity.Image;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Post;
import rs.ac.uns.ftn.svtvezbe07.model.entity.User;

@Getter
@Setter
public class PostDTO {

	private Long id;
	private String content;
	private LocalDateTime creationDate;
	private Set<Image> images;
	private User postedBy;
	  
    public PostDTO() {
        
    }
	

	public Set<Image> getImages() {
		return images;
	}


	public void setImages(Set<Image> images) {
		this.images = images;
	}


	public PostDTO(Post post) {
		this(post.getId(),post.getContent(),post.getCreationDate(),post.getPostedBy(),post.getImages());
	}
	public PostDTO(Long id, String content, LocalDateTime creationDate, User postedBy, Set<Image> set) {
		super();
		this.id = id;
		this.content = content;
		this.creationDate = creationDate;
		this.postedBy = postedBy;
		this.images = set;
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
