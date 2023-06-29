package rs.ac.uns.ftn.svtvezbe07.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.ftn.svtvezbe07.model.entity.User;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserDTO {
	
	private Integer id;
	@NotBlank
	private String username;
	@NotBlank
	private String password;
	private String email;
	private LocalDateTime lastLogin;
	private String firstName;
	private String lastName;
	private String displayName;
	private String description;
	public UserDTO() {
		
	}
	
	public UserDTO(User user) {
		this(user.getId(),user.getUsername(),user.getPassword(),user.getEmail(),user.getLastLogin(), 
				user.getFirstName(), user.getLastName(),user.getDisplayName(),user.getDescription());
	}
	
	public UserDTO(Integer id, String username, String password, String email, LocalDateTime lastLogin,
			String firstName, String lastName, String displayName, String description) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.lastLogin = lastLogin;
		this.firstName = firstName;
		this.lastName = lastName;
		this.displayName = displayName;
		this.description = description;
	}

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
	
/*
    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public UserDTO(User createdUser) {
        this.id = createdUser.getId();
        this.username = createdUser.getUsername();
    }

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
*/
	
}
