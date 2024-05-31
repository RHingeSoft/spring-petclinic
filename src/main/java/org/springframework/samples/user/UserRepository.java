package org.springframework.samples.petclinic.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Repository class for <code>User</code> domain objects. All method names are compliant
 * with Spring Data naming conventions so this interface can easily be extended for Spring
 * Data.
 */
public interface UserRepository extends Repository<User, Integer> {

	/**
	 * Retrieve {@link User}s from the data store by last name, returning all users whose
	 * last name <i>contains</i> the given name.
	 * @param lastName Value to search for
	 * @return a Collection of matching {@link User}s (or an empty Collection if none
	 * found)
	 */
	@Query("SELECT DISTINCT user FROM User user WHERE user.lastName LIKE %:lastName%")
	@Transactional(readOnly = true)
	Page<User> findByLastName(@Param("lastName") String lastName, Pageable pageable);

	/**
	 * Retrieve a {@link User} from the data store by id.
	 * @param id the id to search for
	 * @return the {@link User} if found
	 */
	@Query("SELECT user FROM User user WHERE user.id = :id")
	@Transactional(readOnly = true)
	Optional<User> findById(@Param("id") Integer id);

	/**
	 * Save a {@link User} to the data store, either inserting or updating it.
	 * @param user the {@link User} to save
	 */
	User save(User user);

	/**
	 * Returns all the users from the data store.
	 */
	@Query("SELECT user FROM User user")
	@Transactional(readOnly = true)
	Page<User> findAll(Pageable pageable);

	/**
	 * Retrieve a {@link User} from the data store by username.
	 * @param username the username to search for
	 * @return an Optional containing the {@link User} if found
	 */
	@Query("SELECT user FROM User user WHERE user.username = :username")
	@Transactional(readOnly = true)
	Optional<User> findByUsername(@Param("username") String username);

}
