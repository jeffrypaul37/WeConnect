package com.example.weconnect.weConnectApp.service;

import com.webapp.weconnect.model.Comment;
import com.webapp.weconnect.repository.CommentRepository;
import com.webapp.weconnect.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    public void setUp() {
        commentRepository.equals(this);
    }

    /**
     * Test to check the functionality of save comment method.
     * Checking if the expected result is same as the actual result.
     * */
    @Test
    public void testSaveComment() {
        // Arrange
        Comment comment = new Comment();
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // Calling the method
        Comment savedComment = commentService.saveComment(comment);

        // Assert the result
        assertEquals(comment, savedComment);
    }

    /**
     * Test to check if a comment can save with specific content. Testing is done using the mock objects
     * and a dummy string is provided as an input for testing.
     * */
    @Test
    public void testSaveCommentWithSpecificContent() {
        // Arrange
        String expectedString = "Dummy comment";
        Comment comment = mock(Comment.class); // Mock the Comment object
        when(comment.getContent()).thenReturn(expectedString); // Mock the behavior of getContent() method

        when(commentRepository.save(comment)).thenReturn(comment); // Mock the behavior of save() method

        // Calling the method
        Comment savedComment = commentService.saveComment(comment);

        // Assert
        assertEquals(expectedString, savedComment.getContent());
    }

    /**
     * Test to check the condition when the commnet content is null.
     * If a content is null, then it is tested using assertNull.
     * */
    @Test
    public void testSaveCommentWithNullContent() {
        // Arrange
        Comment comment = new Comment();
        comment.setContent(null);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // Calling the method
        Comment savedComment = commentService.saveComment(comment);

        // Assert
        assertNull(savedComment.getContent());
    }
}
