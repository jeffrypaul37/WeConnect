package com.webapp.weconnect.service;

import com.webapp.weconnect.model.Comment;
import com.webapp.weconnect.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    /**
     * Constructs a CommentService with the necessary dependencies.
     * @param commentRepository repository responsible for comment data operations.
     * */
    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    /**
     * Saves a comment to the database. It delegates the save operation to the commentRepository.
     * @param comment the comment entity to be saved.
     * @return The saved comment entity
     * */
    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

}
