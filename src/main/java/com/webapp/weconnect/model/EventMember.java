package com.webapp.weconnect.model;

import javax.persistence.*;

@Entity
@Table(name = "event_members")
public class EventMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "communityname")
    private String communityName;

    @Column(name = "eventname")
    private String eventName;

    @Column(name = "eventmember")
    private String eventMember;

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public Long getEventId() {
        return id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventMember() {
        return eventMember;
    }

    public void setEventMember(String eventMember) {
        this.eventMember = eventMember;
    }

}
