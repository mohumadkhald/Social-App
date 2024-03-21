package com.projects.socialapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstname;
    private String lastname;
    private String gender;
    private String email;
    private String password;

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

    
}
