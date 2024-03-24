package com.projects.socialapp.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.projects.socialapp.token.Token;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Table(name = "users")
public class User extends Base implements UserDetails {
    private Integer id;
    private String firstname;
    private String lastname;
    private String gender;
    private String phone;
    private String email;
    private String password;
    private boolean rememberMe;



    @Setter
    private boolean accountNonExpired;
    @Setter
    private boolean accountNonLocked;
    @Setter
    private boolean credentialsNonExpired;
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;


    @OneToMany(
            mappedBy = "user"
    )
    @JsonManagedReference
    private List<Post> posts;

    @OneToMany(
            mappedBy = "user"
    )
    @JsonManagedReference
    private List<Comment> comments;


    @ManyToMany(mappedBy = "likedByUsers", cascade = CascadeType.ALL)
    private Set<Post> likedPosts = new HashSet<>();


    @ManyToMany(mappedBy = "likedByUsers", cascade = CascadeType.ALL)
    private Set<Comment> likedComments = new HashSet<>();

    // very important
    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, email); // Include relevant fields for uniqueness
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_followers",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id"))
    private Set<User> followers;

    @ManyToMany(mappedBy = "followers", fetch = FetchType.EAGER)
    private Set<User> followings;

    @Transient
    private boolean friends;
    @PostLoad
    private void calculateFriends() {
        friends = followers.stream().anyMatch(follower -> followings.contains(follower));
    }

    public User(Long id, String firstname, String lastname, String email, String password) {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
