package org.springframework.samples.petclinic.user;

import org.springframework.core.style.ToStringCreator;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.util.Assert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Simple JavaBean domain object representing a user.
 */
@Entity
@Table(name = "users")
public class User extends BaseEntity {

	@Column(name = "username", unique = true)
	@NotBlank
	@Size(min = 3, max = 50)
	private String username;

	@Column(name = "first_name")
	@NotBlank
	private String firstName;

	@Column(name = "last_name")
	@NotBlank
	private String lastName;

	@Column(name = "password")
	@NotBlank
	@Size(min = 8)
	private String password;

	@Column(name = "auth_token")
	private String authToken;

	@Column(name = "email", unique = true)
	@NotBlank
	@Email
	private String email;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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
		return new ToStringCreator(this).append("id", this.getId())
			.append("new", this.isNew())
			.append("username", this.username)
			.append("firstName", this.firstName)
			.append("lastName", this.lastName)
			.append("email", this.email)
			.toString();
	}

	/**
	 * Validate the user's password and authToken.
	 * @param password the password to validate, must not be {@literal null}.
	 * @param authToken the authToken to validate, must not be {@literal null}.
	 */
	public void validateCredentials(String password, String authToken) {
		Assert.notNull(password, "Password must not be null!");
		Assert.notNull(authToken, "Auth token must not be null!");

		if (!this.password.equals(password)) {
			throw new IllegalArgumentException("Invalid password!");
		}
		if (!this.authToken.equals(authToken)) {
			throw new IllegalArgumentException("Invalid auth token!");
		}
	}

}
