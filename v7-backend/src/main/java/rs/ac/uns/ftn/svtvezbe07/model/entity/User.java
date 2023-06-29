package rs.ac.uns.ftn.svtvezbe07.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "users")
public class User {

   /* @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Roles role;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Roles getRole() {
		return role;
	}

	public void setRole(Roles role) {
		this.role = role;
	}
*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "username", nullable = false, unique=true)
	private String username;

	@Column(name = "password", nullable = false)
	private String password;
	
	@Column(name = "email", nullable = false)
	private String email;
	
	@Column(name = "lastLogin", nullable = true)
	private LocalDateTime lastLogin;
	
	@Column(name = "firstName", nullable = false)
	private String firstName;
	
	@Column(name = "lastName", nullable = false)
	private String lastName;
	
	@Column(name = "displayName", nullable = false)
	private String displayName;

	@Column(name = "description", nullable = true)
	private String description;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Roles role;
	
	//@OneToMany(fetch = FetchType.EAGER)
	//private Set<User> friendList = new HashSet<User>();
	@JsonIgnoreProperties("friends")
	@OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_friends",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private Set<User> friends = new HashSet<>();

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDateTime getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(LocalDateTime lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Roles getRole() {
		return role;
	}

	public void setRole(Roles role) {
		this.role = role;
	}

	public Set<User> getFriends() {
		return friends;
	}

	public void setFriends(Set<User> friends) {
		this.friends = friends;
	}

	public User() {

	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		User t = (User) o;
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

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", email=" + email
				+ ", lastLogin=" + lastLogin + ", firstName=" + firstName + ", lastName=" + lastName + ", displayName="
				+ displayName + ", description=" + description + ", role=" + role + ", friends=" + friends + "]";
	}

	

}
