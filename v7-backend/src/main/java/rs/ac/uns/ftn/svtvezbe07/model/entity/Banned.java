package rs.ac.uns.ftn.svtvezbe07.model.entity;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "banned")
public class Banned {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@JsonIgnoreProperties({"friends","description","password","lastLogin","email"})
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="user_id")
	private User towards;
	
	@JsonIgnoreProperties({"friends","description","password","lastLogin","email"})
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="admin_id")
	private User byAdmin;
	
	@JsonIgnoreProperties({"friends","description","password","lastLogin","email"})
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="groupAdmin_id")
	private User byGroupAdmin;
	
	@JsonIgnoreProperties("posts")
	@JoinColumn(name="group_id")
	@OneToOne(fetch = FetchType.EAGER)
	private Group group;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getTowards() {
		return towards;
	}

	public void setTowards(User towards) {
		this.towards = towards;
	}

	public User getByAdmin() {
		return byAdmin;
	}

	public void setByAdmin(User byAdmin) {
		this.byAdmin = byAdmin;
	}

	public User getByGroupAdmin() {
		return byGroupAdmin;
	}

	public void setByGroupAdmin(User byGroupAdmin) {
		this.byGroupAdmin = byGroupAdmin;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}
	
	
}
