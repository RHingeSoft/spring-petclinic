package org.springframework.samples.petclinic.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import jakarta.validation.Valid;

import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
class EmployeeController {

	private final EmployeeRepository employeeRepository;

	@Autowired
	public EmployeeController(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@PostMapping
	public ResponseEntity<Employee> createEmployee() {
		// @Valid @RequestBody Employee employee, BindingResult result
		// if (result.hasErrors()) {
		// throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Validation errors");
		// }
		Employee employee = new Employee();
		employee.setUsername("jdoe");
		employee.setFirstName("John");
		employee.setLastName("Doe");
		employee.setCargo("Developer");
		employee.setSalarioBase(50000);
		employee.setTipoContrato("Full-time");
		employee.setListaDeProyectos("Project A, Project B");

		// Guardar el empleado en el repositorio
		Employee savedEmployee = employeeRepository.save(employee);

		// Devolver la respuesta con el estado 201 Created y la URI del nuevo recurso
		return ResponseEntity
			.created(ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedEmployee.getId())
				.toUri())
			.body(savedEmployee);
	}

	// @GetMapping("/{id}")
	// public ResponseEntity<Employee> getEmployeeById(@PathVariable Integer id) {
	// Optional<Employee> employee = employeeRepository.findById(id);
	// if (employee.isEmpty()) {
	// throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
	// }
	// return ResponseEntity.ok(employee.get());
	// }

	// @PutMapping("/{id}")
	// public ResponseEntity<Employee> updateEmployee(@PathVariable Integer id,
	// @Valid @RequestBody Employee employeeDetails, BindingResult result) {
	// if (result.hasErrors()) {
	// throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Validation errors");
	// }
	// Optional<Employee> optionalEmployee = employeeRepository.findById(id);
	// if (optionalEmployee.isEmpty()) {
	// throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
	// }

	// Employee existingEmployee = optionalEmployee.get();
	// existingEmployee.setUsername(employeeDetails.getUsername());
	// existingEmployee.setFirstName(employeeDetails.getFirstName());
	// existingEmployee.setLastName(employeeDetails.getLastName());
	// existingEmployee.setCargo(employeeDetails.getCargo());
	// existingEmployee.setSalarioBase(employeeDetails.getSalarioBase());
	// existingEmployee.setTipoContrato(employeeDetails.getTipoContrato());
	// existingEmployee.setListaDeProyectos(employeeDetails.getListaDeProyectos());

	// Employee updatedEmployee = employeeRepository.save(existingEmployee);
	// return ResponseEntity.ok(updatedEmployee);
	// }

	// @DeleteMapping("/{id}")
	// public ResponseEntity<Employee> deleteEmployee(@PathVariable Integer id) {
	// Optional<Employee> optionalEmployee = employeeRepository.findById(id);
	// if (optionalEmployee.isEmpty()) {
	// throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
	// }
	// employeeRepository.deleteById(id);
	// return ResponseEntity.noContent().build();
	// }

	// @GetMapping("/")
	// // *Este mentodo trea todos*/
	// public ResponseEntity<Page<Employee>> getAllEmployees(Pageable pageable) {
	// Page<Employee> employees = employeeRepository.findAll(pageable);
	// return ResponseEntity.ok(employees);
	// }

}