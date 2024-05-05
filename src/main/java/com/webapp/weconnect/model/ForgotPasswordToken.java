package com.webapp.weconnect.model;

import javax.persistence.*;
import java.time.LocalDateTime;


/**
 * Class to create the table in database to store the information related to password
 * like the id, token , its relation with other parameters,etc.
 *
 * @author Luv Patel
 * @verison 1.0
 * */
@Entity
public class ForgotPasswordToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String token;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name="user_id")
    private User user;

    @Column(nullable = false)
    private LocalDateTime expireTime;

    @Column(nullable = false)
    private boolean isUsed;


    // Getter and setter for the fields of table
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public LocalDateTime getExpireTime() {
        return expireTime;
    }
    public void setExpireTime(LocalDateTime expireTime){
        this.expireTime = expireTime;
    }
    public boolean isUsed(){
        return isUsed;
    }
    public void setUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }

}
