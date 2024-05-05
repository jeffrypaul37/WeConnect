package com.example.weconnect.weConnectApp.service;

import com.webapp.weconnect.service.PostService;
import com.webapp.weconnect.model.Message;
import com.webapp.weconnect.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private MultipartFile postImage;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createPost_shouldSavePostCorrectly() throws IOException {
        // Arrange
        String postTitle = "Test Post";
        String postContent = "This is a test post content.";
        byte[] imageBytes = {1, 2, 3, 4};
        String communityName = "TestCommunity";
        when(postImage.getBytes()).thenReturn(imageBytes);

        // Act
        postService.createPost(postTitle, postContent, postImage, communityName);

        // Assert
        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        verify(postRepository).save(messageCaptor.capture());
        Message savedMessage = messageCaptor.getValue();
        assertEquals(postTitle, savedMessage.getTitle());
    }

    @Test
    public void getPostImageById_whenPostExists_shouldReturnImage() {
        // Arrange
        Long postId = 1L;
        byte[] expectedImage = {1, 2, 3, 4};
        Message mockMessage = new Message();
        mockMessage.setPostImage(expectedImage);
        when(postRepository.findById(postId)).thenReturn(Optional.of(mockMessage));

        // Act
        byte[] actualImage = postService.getPostImageById(postId);

        // Assert
        assertArrayEquals(expectedImage, actualImage, "The returned image bytes should match the expected bytes.");
    }

    @Test
    public void getPostImageById_whenPostDoesNotExist_shouldReturnNull() {
        // Arrange
        Long postId = 2L;
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // Act
        byte[] actualImage = postService.getPostImageById(postId);

        // Assert
        assertNull(actualImage, "The image should be null when the post does not exist.");
    }

    @Test
    public void getAllPosts_shouldReturnAllPosts() {
        // Arrange
        Message post1 = new Message();
        Message post2 = new Message();
        List<Message> expectedPosts = Arrays.asList(post1, post2);
        when(postRepository.findAll()).thenReturn(expectedPosts);

        // Act
        List<Message> actualPosts = postService.getAllPosts();

        // Assert
        assertEquals(expectedPosts, actualPosts, "The returned posts should match the expected posts.");
    }
}
