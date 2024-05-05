package com.webapp.weconnect.repository;

import com.webapp.weconnect.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Message, Long> {

    /**
     * To fetch the posts associated to community based on its ID.
     * @param id the ID of post for which the record needs to be fetched.
     * @return optional post if found.
     * */
    Optional<Message> findById(Long id);

}
