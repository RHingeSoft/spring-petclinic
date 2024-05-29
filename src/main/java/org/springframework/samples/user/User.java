package org.springframework.samples.petclinic.user;

import org.springframework.samples.petclinic.model.Person;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User extends Person {

	@Column(name = "username", unique = true, nullable = false, length = 50)
	@NotBlank
	private String username;

	@Column(name = "password", nullable = false, length = 255)
	@NotBlank
	private String password;

	@Column(name = "auth_token", length = 255)
	private String authToken;

	@Column(name = "email", unique = true, length = 100)
	private String email;

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

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "User{" + "id=" + getId() + ", username='" + username + '\'' + ", password='" + password + '\''
				+ ", authToken='" + authToken + '\'' + ", email='" + email + '\'' + ", lastName='" + getLastName()
				+ '\'' + ", firstName='" + getFirstName() + '\'' + '}';
	}

}
