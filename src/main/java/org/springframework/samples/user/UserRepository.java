package org.springframework.samples.petclinic.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

	@Query("SELECT u FROM User u WHERE u.username = :username")
	@Transactional(readOnly = true)
	Optional<User> findByUsername(String username);

	@Query("SELECT u FROM User u WHERE u.username = :username AND u.password = :password")
	@Transactional(readOnly = true)
	Optional<User> findByUsernameAndPassword(String username, String password);

}
