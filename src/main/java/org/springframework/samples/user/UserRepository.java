package org.springframework.samples.petclinic.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends Repository<User, Integer> {

	@Query("SELECT u FROM User u WHERE u.username = :username")
	@Transactional(readOnly = true)
	User findByUsername(@Param("username") String username);

	@Query("SELECT u FROM User u")
	@Transactional(readOnly = true)
	List<User> findAll();

	@Query("SELECT u FROM User u WHERE u.username = :username AND u.password = :password")
	@Transactional(readOnly = true)
	User findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

	void save(User user);

	Page<User> findAll(Pageable pageable);

}
