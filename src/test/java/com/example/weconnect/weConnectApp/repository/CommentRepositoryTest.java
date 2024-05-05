package com.example.weconnect.weConnectApp.repository;

import com.webapp.weconnect.model.Comment;
import com.webapp.weconnect.model.Message;
import com.webapp.weconnect.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class CommentRepositoryTest {

    /**
     * Test to check the condition to fetch the comment by messageID
     * Validating the expected size of the number of comments returned
     * */
    @Test
    void testFindByMessageId() {

        // Mock the objects for testing
        CommentRepository commentRepositoryMock = Mockito.mock(CommentRepository.class);
        Comment commentMock = Mockito.mock(Comment.class);
        //Arrange
        Long messageID = 1L;
        commentMock.setId(1L);
        commentMock.setContent("Hello World!!");
        commentMock.setCreatedAt(LocalDateTime.now());
        commentMock.setMessage(new Message());

        // Mock the behavior of the findByMessageId method
        when(commentRepositoryMock.findByMessageId(messageID)).thenReturn(Collections.singletonList(commentMock));

        // Calling the method
        List<Comment> comments = commentRepositoryMock.findByMessageId(messageID);

        // Assert
        assertEquals(1, comments.size());

    }

    /**
     * Test to check the functionality when there are no comments associated with a message
     * In this case, it should return a empty list
     * */
    @Test
    void testFindByMessageID_NoCommentFound() {
        Long messageID = 2L;
        CommentRepository commentRepositoryMock = Mockito.mock(CommentRepository.class);
        when(commentRepositoryMock.findByMessageId(messageID)).thenReturn(Collections.emptyList());

        // Calling the method
        List<Comment> comments = commentRepositoryMock.findByMessageId(messageID);

        //Assert
        assertTrue(comments.isEmpty());
    }

    /**
     * Test to check the functionality when trying to find the comments when the messageID is null.
     * This should not return any comment and list size should be zero.
     * */
    @Test
    void testFindByMessageID_NullMessageID() {
        Long MessageID = null;

        CommentRepository commentRepositoryMock = Mockito.mock(CommentRepository.class);
        List<Comment> extractedComment = commentRepositoryMock.findByMessageId(MessageID);

        assertEquals(0, extractedComment.size());
        verify(commentRepositoryMock, never()).findByMessageId(anyLong());
    }
}
