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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "grupe")
public class Group {
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column
	    private Long id;

	    @Column
	    private String name;

	    @Column
	    private String description;

	    @Column (name="creation_date")
	    private LocalDateTime creationDate;

		@Column(columnDefinition = "BOOL",name="isdeleted")
		private boolean isDeleted;

	    @OnDelete(action = OnDeleteAction.CASCADE) 
	    @JsonIgnoreProperties({"username","friends","groups", "description","password","lastLogin","role","email"})
		@JoinColumn(name="user_id")
		@ManyToOne(fetch = FetchType.EAGER)
		private User groupAdmin;
	    
	    @JsonIgnoreProperties({"friends","groups", "lastName", "firstName","description","password","lastLogin","role"})
	    @ManyToMany(fetch = FetchType.EAGER)
	    @JoinTable(
	        name = "group_members",
	        joinColumns = @JoinColumn(name = "group_id"),
	        inverseJoinColumns = @JoinColumn(name = "user_id")
	    )
	    private Set<User> members = new HashSet<>();

	    @JsonIgnore//Properties({"group","postedBy","reactions"})
		@OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL,orphanRemoval = true)
	    @JoinTable(
	        name = "group_posts",
	        joinColumns = @JoinColumn(name = "group_id"),
	        inverseJoinColumns = @JoinColumn(name = "post_id")
	    )
		@Cascade(org.hibernate.annotations.CascadeType.ALL)
	    private Set<Post> posts = new HashSet<>();

	    @Column(columnDefinition = "BOOL")
	    private boolean suspended;
	    
	    @Column
	    private String suspendedReason;


		public Group() {
			super();
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public LocalDateTime getCreationDate() {
			return creationDate;
		}

		public void setCreationDate(LocalDateTime creationDate) {
			this.creationDate = creationDate;
		}

		public Set<Post> getPosts() {
			return posts;
		}

		public void setPosts(Set<Post> posts) {
			this.posts = posts;
		}


		public User getGroupAdmin() {
			return groupAdmin;
		}

		public void setGroupAdmin(User groupAdmin) {
			this.groupAdmin = groupAdmin;
		}

		public boolean isSuspended() {
			return suspended;
		}

		public void setSuspended(boolean suspended) {
			this.suspended = suspended;
		}

		public String getSuspendedReason() {
			return suspendedReason;
		}

		public void setSuspendedReason(String suspendedReason) {
			this.suspendedReason = suspendedReason;
		}

		public boolean isDeleted() {
			return isDeleted;
		}

		public void setDeleted(boolean isDeleted) {
			this.isDeleted = isDeleted;
		}

		public Set<User> getMembers() {
			return members;
		}

		public void setMembers(Set<User> members) {
			this.members = members;
		}


		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			Group t = (Group) o;
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
