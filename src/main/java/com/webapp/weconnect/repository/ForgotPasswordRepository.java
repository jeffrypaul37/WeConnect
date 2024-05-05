package com.webapp.weconnect.repository;

import com.webapp.weconnect.model.ForgotPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface for the forgot password, which contains the method signature for
 *  the findByToken method to be used in various places.
 * @author Luv Patel
 * @version 1.0
 * */
@Repository
public interface ForgotPasswordRepository extends JpaRepository<ForgotPasswordToken, Long> {

    /**
     * To find the token for the password reset link provied to reset password.
     * So that the link can be verified if it is active, used or expired.
     * @param token the unique token generated upon reset request.
     * */
    ForgotPasswordToken findByToken(String token);
}
