package dev.mertkaanguzel.mediumclone.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Objects;

@Entity
@Table(name = "Users")
public class UserAccount {
    @Id
    @GeneratedValue
    private Long id;

    private String username;
    @JsonIgnore
    private String password;

    private String email;
    private String bio;
    private String image;

    public UserAccount(String username, String password, String email, String bio, String image) {
        this.id = null;
        this.username = username;
        this.password = password;
        this.email = email;
        this.bio = bio;
        this.image = image;
    }

    protected UserAccount() {
    }

    public UserDetails asUser() {
        return  User
                .withUsername(getUsername())
                .password(/*"{noop}" + */getPassword()) //new BCryptPasswordEncoder(16).encode(getPassword())
                .authorities("ROLE_USER") // just one role for sake of jwt
                .build();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
