package ar.uba.fi.ingsoft1.todo_template.user;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity(name = "users")
public class User implements UserDetails, UserCredentials {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    @ElementCollection
    private List<Long> followers = new ArrayList<>();
    @ElementCollection
    private List<Long> following = new ArrayList<>();

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.role = "USER";
    }

    @Override
    public String username() {
        return this.username;
    }

    @Override
    public String password() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public String getRole() {
        return role;
    }

    public Long getId(){return id;}

    public List<Long> getFollowers(){return followers;}
    public List<Long> getFollowing(){return following;}

    public void addFollower(Long id){followers.add(id);}
    public void addFollowing(Long id){following.add(id);}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }
}
