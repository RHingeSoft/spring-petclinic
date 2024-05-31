package org.springframework.samples.petclinic.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.samples.petclinic.utils.JsonFileHandler;
import jakarta.validation.Valid;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/entrevistapsicologica")
class EntrevistaPsicologicaController {

	private final String filePath = "src/main/resources/db/JSON/EntrevistaPsicologica.json";

	@Autowired
	private JsonFileHandler jsonFileHandler;

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@PostMapping
	public ResponseEntity<Map<String, Object>> createEntrevistaPsicologica(
			@Valid @RequestBody Map<String, Object> entrevista) {
		try {
			List<Map<String, Object>> employees = jsonFileHandler.readFile(filePath);
			// debe asignar un id único al empleado
			entrevista.put("id", employees.size() + 1);
			employees.add(entrevista);
			jsonFileHandler.writeFile(employees, filePath);
			return ResponseEntity.ok(entrevista);
		}
		catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error writing to file", e);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> getEntrevistaById(@PathVariable Integer id) {
		try {
			Optional<Map<String, Object>> entrevista = jsonFileHandler.findById(id, filePath);
			if (entrevista.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entrevista not found");
			}
			return ResponseEntity.ok(entrevista.get());
		}
		catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reading from file", e);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> updateEntrevista(@PathVariable Integer id,
			@Valid @RequestBody Map<String, Object> entrevistaDetails, BindingResult result) {
		if (result.hasErrors()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Validation errors");
		}
		try {
			List<Map<String, Object>> entrevista = jsonFileHandler.readFile(filePath);
			Optional<Map<String, Object>> optionalEntrevista = entrevista.stream()
				.filter(e -> e.get("id").equals(id))
				.findFirst();
			if (optionalEntrevista.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entrevista not found");
			}

			Map<String, Object> existingEntrevista = optionalEntrevista.get();
			existingEntrevista.putAll(entrevistaDetails);

			jsonFileHandler.writeFile(entrevista, filePath);
			return ResponseEntity.ok(existingEntrevista);
		}
		catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error writing to file", e);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteEntrevista(@PathVariable Integer id) {
		try {
			List<Map<String, Object>> entrevistas = jsonFileHandler.readFile(filePath);
			boolean removed = entrevistas.removeIf(e -> e.get("id").equals(id));
			if (!removed) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entrevista not found");
			}
			jsonFileHandler.writeFile(entrevistas, filePath);
			return ResponseEntity.noContent().build();
		}
		catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error writing to file", e);
		}
	}

	@GetMapping("/")
	public ResponseEntity<List<Map<String, Object>>> getAllEntrevista() {
		try {
			List<Map<String, Object>> entrevistas = jsonFileHandler.readFile(filePath);
			return ResponseEntity.ok(entrevistas);
		}
		catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reading from file", e);
		}
	}

}
