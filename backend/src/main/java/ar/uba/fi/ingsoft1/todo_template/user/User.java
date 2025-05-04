package ar.uba.fi.ingsoft1.todo_template.user;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
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

    @Column(nullable = false)
    private String email;

    private String firstName;

    private String lastName;

    private String avatarUrl;

    @Column(nullable = false)
    private LocalDate birthDate;

    @ElementCollection
    private List<Long> followers = new ArrayList<>();
    @ElementCollection
    private List<Long> following = new ArrayList<>();

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.role = "ROLE_USER";
    }

    public User(String username, String password, String role, String email,
                String firstName, String lastName, String avatarUrl, LocalDate birthDate) {
        this.username = username;
        this.password = password;
        this.role = role != null ? role : "USER"; // default a "USER" si viene null
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatarUrl = avatarUrl;
        this.birthDate = birthDate;
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
    public void setUsername(String username){this.username = username; }

    @Override
    public String getPassword() {
        return this.password;
    }

    public String getRole() {
        return role;
    }

    public void promover(){
        this.role = "ROLE_ADMIN";
    }

    public Long getId(){return id;}

    public List<Long> getFollowers(){return followers;}
    public List<Long> getFollowing(){return following;}

    public void addFollower(Long id){followers.add(id);}
    public void addFollowing(Long id){following.add(id);}

    public String getEmail() { return this.email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }
}
