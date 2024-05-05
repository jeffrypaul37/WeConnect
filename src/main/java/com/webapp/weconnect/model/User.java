package com.webapp.weconnect.model;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;

    private String password;

    private String location;

    @Lob
    @Column(name = "profile_photo", columnDefinition="LONGBLOB")
    private byte[] profile_photo;

    @OneToMany(mappedBy = "creator")
    private List<Community> communities;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    // New Fields added from the first version
    private boolean enabled = false; // New users are unauthenticated by default
    private String verificationToken; // For storing email verification tokens

    public User() {
    }

    public User(String firstName, String lastName, String email, String password, Collection<Role> roles, String location, byte[] profile_photo) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.location = location;
        this.profile_photo = profile_photo;
        // The constructor does not initialize 'enabled' or 'verificationToken' as they are expected to be set separately
    }

    public <T> User(String firstName, String lastName, String email, String encode, List<T> roleUser, String location, byte[] profilePhoto) {
    }

    // Getters and setters for all fields including 'enabled' and 'verificationToken'
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocation() { return location; }
    public void setLocation(String location) {this.location = location; }
    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public byte[] getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(byte[] profile_photo) {
        this.profile_photo = profile_photo;
    }

}
