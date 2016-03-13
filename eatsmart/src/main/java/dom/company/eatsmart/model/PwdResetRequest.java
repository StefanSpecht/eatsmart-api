package dom.company.eatsmart.model;

import org.hibernate.validator.constraints.NotBlank;

public class PwdResetRequest {

	@NotBlank(message="Username must not be null or blank")
	private String username;
	
	@NotBlank(message="Email must not be null or blank")
	private String email;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
