package com.webapp.weconnect.model;

import javax.persistence.*;

@Entity
@Table(name = "community_members")
public class CommunityMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "community_id", referencedColumnName = "id")
    private Community community;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private String member;

    private String members;

    public String getMembers() {
        return members;
    }

}
