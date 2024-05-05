package com.webapp.weconnect.repository;

import com.webapp.weconnect.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * JPA Method to find the comments for a particular post using the message ID.
     * @param messageId the ID of the message for which the comments are required.
     * */
    List<Comment> findByMessageId(Long messageId);
}
