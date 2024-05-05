package com.webapp.weconnect.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "event")
public class Event {

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

    @Column(name = "communityname")
    private String communityName;

    @Column(name = "event_date")
    private Date eventDate;

    @Column(name = "event_time")
    private String eventTime;

    @Column(name = "max_capacity")
    private Integer maxCapacity;

    @Column(name = "location")
    private String location;

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

    public byte[] getEventImage() {
        return image;
    }

    public void setEventImage(byte[] image) {
        this.image = image;
    }

    public String getCommunityname() {
        return communityName;
    }

    public void setCommunityname(String communityName) {
        this.communityName = communityName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public boolean hasImage() {
        return image != null && image.length > 0;
    }

}
