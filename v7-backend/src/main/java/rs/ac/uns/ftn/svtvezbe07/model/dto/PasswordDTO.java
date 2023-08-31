package rs.ac.uns.ftn.svtvezbe07.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;



public class PasswordDTO {

    @NotBlank
    private String username;
    @NotBlank
    private String current;
    @NotBlank
    private String password;
    @NotBlank
    private String confirm;

    public PasswordDTO(String username, String current, String password, String confirm) {
        this.username = username;
        this.current = current;
        this.password = password;
        this.confirm = confirm;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCurrent() {
        return current;
    }
    public void setCurrent(String current) {
        this.current = current;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String newPW) {
        this.password = newPW;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

	public PasswordDTO() {
		super();
	}
    
}
