package com.webapp.weconnect.model;

import javax.persistence.*;

@Entity
@Table(name = "image")
public class CommunityImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "image", columnDefinition="LONGBLOB")
    private byte[] image;
    @Lob
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @Column(name = "communityname")
    private String communityName;

    public byte[] getCommunityImage() {
        return image;
    }

    public void setCommunityImage(byte[] image) {
        this.image = image;
    }
    public String getCommunityDescription() {
        return description;
    }

    public void setCommunityDescription(String description) {
        this.description = description;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

}
