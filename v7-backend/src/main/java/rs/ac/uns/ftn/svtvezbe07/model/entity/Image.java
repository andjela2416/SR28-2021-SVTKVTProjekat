package rs.ac.uns.ftn.svtvezbe07.model.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "images")
public class Image {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;	
	
	@Column(name = "image_path")
	private String imagePath;
	
//	@Column(name = "name")
//	private String name;

	@Column(name = "type")
	private String type;

    //image bytes can have large lengths so we specify a value
    //which is more than the default length for picByte column
	@Column(name = "picByte", length = 10000000)
	private byte[] picByte;
	
	


//	public String getName() {
//		return name;
//	}
//
//
//	public void setName(String name) {
//		this.name = name;
//	}


	public String getImagePath() {
		return imagePath;
	}


	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	

	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public byte[] getPicByte() {
		return picByte;
	}


	public void setPicByte(byte[] picByte) {
		this.picByte = picByte;
	}


//	@OnDelete(action = OnDeleteAction.CASCADE) 
//	@JsonIgnoreProperties({"content","creationDate","images","postedBy"})
//	@JoinColumn(name="post_id")
//	@ManyToOne(fetch = FetchType.EAGER)
//	private Post post;
	    
	@JsonIgnoreProperties({"username","friends", "lastName", "firstName","description","password","displayName","lastLogin","role","email"})
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;



	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


//	public Post getPost() {
//		return post;
//	}
//
//
//	public void setPost(Post post) {
//		this.post = post;
//	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}	
	public Image() {
		super();
	}

//	public Image(String name, String type, byte[] picByte) {
//		this.name = name;
//		this.type = type;
//		this.picByte = picByte;
//	}
	public Image(String path, String type, byte[] picByte) {
		this.imagePath = path;
		this.type = type;
		this.picByte = picByte;
	}

//	public Image(String path,/* String name,*/ String type, byte[] picByte, Post post, User user) {
//		super();
//		this.imagePath = path;
//		/*this.name = name;*/
//		this.type = type;
//		this.picByte = picByte;
//		//this.post = post;
//		this.user = user;
//	}


	public Image(String path, String type, byte[] picByte, User user) {
		super();
		this.imagePath = path;
		this.type = type;
		this.picByte = picByte;
		this.user = user;
	}


//	public Image(String path, String type, byte[] picByte) {
//		super();
//		this.imagePath = path;
//		this.type = type;
//		this.picByte = picByte;
//	//	this.post = post;
//	}


	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Image t = (Image) o;
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
