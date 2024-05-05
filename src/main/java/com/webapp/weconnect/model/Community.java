package com.webapp.weconnect.model;

import javax.persistence.*;

@Entity
@Table(name = "community",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_community", columnNames = {"name", "created_by"})
        })
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "creator_id")
    private Long creatorId;

    @Column(name = "creator_email")
    private String creatorEmail;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "is_leader")
    private Boolean isLeader;;

    @ManyToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User creator;

    @Column(name = "is_approved")
    private Boolean isApproved = false;

    // getter和setter
    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }

    public String getName() {
        return name;
    }

    public Long getId() {return id;}

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Boolean getIsLeader() {
        return isLeader;
    }

    public void setIsLeader(Boolean isLeader) {
        this.isLeader = isLeader;
    }

    public void setId(long id) {this.id = id;
    }
}
