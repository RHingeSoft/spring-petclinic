package org.springframework.samples.petclinic.user;

import org.springframework.core.style.ToStringCreator;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Arrays;
import jakarta.persistence.ElementCollection;
import java.util.Set;
import java.util.HashSet;

import java.util.UUID;

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

	@Column(name = "auth_token", unique = true)
	private String authToken;

	@Column(name = "email", unique = true)
	@NotBlank
	@Email
	private String email;

	@Column(name = "roles")
	@ElementCollection(targetClass = String.class)
	private Set<String> roles = new HashSet<>();

	// Password encoder to encode and match passwords
	private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
		this.password = passwordEncoder.encode(password);
	}

	public String getAuthToken() {
		return authToken;
	}

	public Set<String> getRoles() {
		Set<String> roles = new HashSet<>();
		roles.add("ADMIN");
		return roles;
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
			.append("roles", this.roles)
			.toString();
	}

	/**
	 * Validate the user's password.
	 * @param password the password to validate, must not be {@literal null}.
	 */
	public void validatePassword(String password) {
		Assert.notNull(password, "Password must not be null!");
		if (!passwordEncoder.matches(password, this.password)) {
			throw new IllegalArgumentException("Invalid password!");
		}
	}

	/**
	 * Validate the user's authToken.
	 * @param authToken the authToken to validate, must not be {@literal null}.
	 */
	public void validateAuthToken(String authToken) {
		Assert.notNull(authToken, "Auth token must not be null!");
		if (!this.authToken.equals(authToken)) {
			throw new IllegalArgumentException("Invalid auth token!");
		}
	}

	/**
	 * Generate a new authentication token for the user.
	 */
	public void generateAuthToken() {
		this.authToken = UUID.randomUUID().toString();
	}

}
