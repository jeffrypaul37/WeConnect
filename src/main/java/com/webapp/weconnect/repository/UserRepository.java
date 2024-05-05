package com.webapp.weconnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.webapp.weconnect.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	/**
	 * To find the user based on its email address. So that the user details can be used to peform tasks related to user.
	 * @param email the email address of the user which is desired.
	 * @return User that matches the email .
	 * */
	User findByEmail(String email);
}
