package com.webapp.weconnect.service;

import com.webapp.weconnect.model.CommunityImage;
import com.webapp.weconnect.model.Message;
import com.webapp.weconnect.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    /**
     * Creates a new post with the given title, description, image and the community to which it belongs to.
     * Because we need to keep track of which event is organized to which community.
     * @param postTitle the title of the post
     * @param postContent the content that is mentioned in the post.
     * @param postImage the image of the blog post.
     * @param communityname the name of community in which it is created.
     * */
    public void createPost(String postTitle, String postContent, MultipartFile postImage, String communityname) {
        Message post = new Message();
        post.setTitle(postTitle);
        post.setContent(postContent);
        try {
            byte[] imageBytes = postImage.getBytes();
            post.setPostImage(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        post.setCommunityname(communityname);
        postRepository.save(post);
    }

    /**
     * To obtain the image of the blog post by its ID.
     * As the ID is unique for each post image, it is easy to fetch the required data easily.
     * @param id the ID of the post image that is required
     * */
    public byte[] getPostImageById( Long id) {
        Optional<Message> optionalMessage = postRepository.findById(id);
        if (optionalMessage.isPresent()) {
            return optionalMessage.get().getPostImage();
        }
        return null;
    }

    /**
     * Retrieve all the post that are present, as a list
     * @return A list of all the posts that are created by the community leader.
     * */
    public List<Message> getAllPosts() {
        return postRepository.findAll();
    }
}
