package com.webapp.weconnect.repository;

import com.webapp.weconnect.model.User;
import com.webapp.weconnect.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    /**
     * To find the token which was generated during the user registration for verification purposes.
     * So that the user can be verified using the verification link
     * @param token the unique token generated
     *
     * */
    VerificationToken findByToken(String token);
}
