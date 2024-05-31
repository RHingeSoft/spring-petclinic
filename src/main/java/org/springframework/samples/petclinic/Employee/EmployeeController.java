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
import java.util.stream.Collectors;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
class EmployeeController {

	private final String filePath = "src/main/resources/db/JSON/Employees.json";

	@Autowired
	private JsonFileHandler jsonFileHandler;

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@PostMapping
	public ResponseEntity<Map<String, Object>> createEmployee(@Valid @RequestBody Map<String, Object> employee) {
		try {
			List<Map<String, Object>> employees = jsonFileHandler.readFile(filePath);
			// debe asignar un id único al empleado
			employee.put("id", employees.size() + 1);
			employees.add(employee);
			jsonFileHandler.writeFile(employees, filePath);
			return ResponseEntity.ok(employee);
		}
		catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error writing to file", e);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> getEmployeeById(@PathVariable Integer id) {
		try {
			Optional<Map<String, Object>> employee = jsonFileHandler.findById(id, filePath);
			if (employee.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
			}
			return ResponseEntity.ok(employee.get());
		}
		catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reading from file", e);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> updateEmployee(@PathVariable Integer id,
			@Valid @RequestBody Map<String, Object> employeeDetails, BindingResult result) {
		if (result.hasErrors()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Validation errors");
		}
		try {
			List<Map<String, Object>> employees = jsonFileHandler.readFile(filePath);
			Optional<Map<String, Object>> optionalEmployee = employees.stream()
				.filter(e -> e.get("id").equals(id))
				.findFirst();
			if (optionalEmployee.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
			}

			Map<String, Object> existingEmployee = optionalEmployee.get();
			existingEmployee.putAll(employeeDetails);

			jsonFileHandler.writeFile(employees, filePath);
			return ResponseEntity.ok(existingEmployee);
		}
		catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error writing to file", e);
		}
	}

	@GetMapping("/list")
	public ResponseEntity<List<Map<String, Object>>> listEmployees(
			@RequestParam(value = "name", required = false) String name) {
		try {
			List<Map<String, Object>> employees = jsonFileHandler.readFile(filePath);
			List<Map<String, Object>> filteredEmployees;

			if (name != null) {
				// Filtrar empleados por nombre
				filteredEmployees = employees.stream()
					.filter(e -> e.get("name").toString().equalsIgnoreCase(name))
					.collect(Collectors.toList());
			}
			else {
				// Devolver todos los empleados si no se proporciona un nombre para
				// filtrar
				filteredEmployees = employees;
			}

			return ResponseEntity.ok(filteredEmployees);
		}
		catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reading from file", e);
		}
	}

	@DeleteMapping("/list/{id}")
	public ResponseEntity<Void> deleteEmployee(@PathVariable Integer id) {
		try {
			List<Map<String, Object>> employees = jsonFileHandler.readFile(filePath);
			boolean removed = employees.removeIf(e -> e.get("id").equals(id));
			if (!removed) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
			}
			jsonFileHandler.writeFile(employees, filePath);
			return ResponseEntity.noContent().build();
		}
		catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error writing to file", e);
		}
	}

	@GetMapping("/")
	public ResponseEntity<List<Map<String, Object>>> getAllEmployees() {
		try {
			List<Map<String, Object>> employees = jsonFileHandler.readFile(filePath);
			return ResponseEntity.ok(employees);
		}
		catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reading from file", e);
		}
	}

}
