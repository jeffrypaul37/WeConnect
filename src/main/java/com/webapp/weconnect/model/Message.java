package com.webapp.weconnect.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;

    @Lob
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Lob
    @Column(name = "image", columnDefinition="LONGBLOB")
    private byte[] image;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @Column(name = "communityname")
    private String communityName;
    public Long getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getPostImage() {
        return image;
    }

    public void setPostImage(byte[] image) {
        this.image = image;
    }

    public String getCommunityname() {
        return communityName;
    }

    public void setCommunityname(String communityName) {
        this.communityName = communityName;
    }
    public boolean hasImage() {
        return image != null && image.length > 0;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

}
