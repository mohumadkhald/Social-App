package com.projects.socialapp.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstname;
    private String lastname;
    private String gender;
    private String phone;
    private String email;
    private String password;
    @Setter
    private boolean accountNonExpired;
    @Setter
    private boolean accountNonLocked;
    @Setter
    private boolean credentialsNonExpired;
    @Enumerated(EnumType.STRING)
    private Role role;



    @OneToMany(
            mappedBy = "user"
    )
    @JsonManagedReference
    private List<Post> posts;




    @ManyToMany(mappedBy = "likedByUsers", cascade = CascadeType.ALL)
    private Set<Post> likedPosts = new HashSet<>();

















































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
