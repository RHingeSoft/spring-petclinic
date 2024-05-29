
package org.springframework.samples.petclinic.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map; // Añadir esta importación

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserRepository userRepository;

	@Autowired
	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
		String username = loginRequest.get("username");
		String password = loginRequest.get("password");

		User foundUser = userRepository.findByUsernameAndPassword(username, password);
		if (foundUser != null) {
			// Autenticación exitosa, devolver el usuario autenticado
			return ResponseEntity.ok(foundUser);
		}
		else {
			// Autenticación fallida, devolver un mensaje de error
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
		}
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody Map<String, String> registerRequest) {
		String username = registerRequest.get("username");
		String password = registerRequest.get("password");

		// Verificar si el usuario ya existe
		User existingUser = userRepository.findByUsername(username);
		if (existingUser != null) {
			// El usuario ya existe, devolver un mensaje de error
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
		}
		else {
			// Guardar el nuevo usuario en la base de datos
			User newUser = new User();
			newUser.setUsername(username);
			newUser.setPassword(password);
			userRepository.save(newUser);
			// Registro exitoso, devolver el usuario registrado
			return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
		}
	}

}
