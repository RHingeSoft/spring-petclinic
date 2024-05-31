package org.springframework.samples.petclinic.employee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface EmployeeRepository extends Repository<Employee, Integer> {

	@Query("SELECT DISTINCT employee FROM Employee employee WHERE employee.lastName LIKE %:lastName%")
	@Transactional(readOnly = true)
	Page<Employee> findByLastName(@Param("lastName") String lastName, Pageable pageable);

	@Query("SELECT employee FROM Employee employee WHERE employee.id = :id")
	@Transactional(readOnly = true)
	Optional<Employee> findById(@Param("id") Integer id);

	/**
	 * Save a {@link User} to the data store, either inserting or updating it.
	 * @param user the {@link User} to save
	 */
	Employee save(Employee employee);

	@Query("SELECT employee FROM Employee employee")
	@Transactional(readOnly = true)
	Page<Employee> findAll(Pageable pageable);

	/* Metodo para eliminar */
	@Query("DELETE FROM Employee employee WHERE employee.id = :id")
	@Transactional(readOnly = true)
	void deleteById(@Param("id") Integer id);

}
