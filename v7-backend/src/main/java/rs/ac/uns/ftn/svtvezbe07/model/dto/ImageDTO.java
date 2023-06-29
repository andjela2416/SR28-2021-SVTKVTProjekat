package rs.ac.uns.ftn.svtvezbe07.model.dto;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Image;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Post;
import rs.ac.uns.ftn.svtvezbe07.model.entity.User;

@Getter
@Setter
public class ImageDTO {
	private Integer id;	
	private String path;
	private Post post;
	private User user;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Post getPost() {
		return post;
	}
	public void setPost(Post post) {
		this.post = post;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public ImageDTO () {
		
	}
	public ImageDTO(Image image) {
		this(image.getId(),image.getPath(),image.getPost(),image.getUser());
	}
	public ImageDTO(Integer id, String path, Post post, User user) {
		super();
		this.id = id;
		this.path = path;
		this.post = post;
		this.user = user;
	}
	
	
}
