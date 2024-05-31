package org.springframework.samples.petclinic.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing user registrations.
 */
@RestController
@RequestMapping("/api/users")
class UserController {

	private final UserRepository userRepository;

	@Autowired
	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@PostMapping
	public ResponseEntity<User> createUser(@Valid @RequestBody User user, BindingResult result) {
		if (result.hasErrors()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Validation errors");
		}

		User savedUser = userRepository.save(user);

		return ResponseEntity
			.created(ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedUser.getId())
				.toUri())
			.body(savedUser);
	}

	@PostMapping("/login")
	public ResponseEntity<User> loginUser(@RequestBody Map<String, String> credentials) {
		String username = credentials.get("username");
		String password = credentials.get("password");

		if (username == null || password == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username and password must be provided");
		}

		Optional<User> optionalUser = userRepository.findByUsername(username);
		if (optionalUser.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
		}

		User user = optionalUser.get();

		try {
			user.validatePassword(password);
		}
		catch (IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
		}

		return ResponseEntity.ok(user);
	}

}
