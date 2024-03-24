package com.projects.socialapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"comments", "likedByUsers"})
@Table(name = "reels")
public class Reel extends Base {
    private String title;
    private String video;

    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonBackReference
    private User user;

//    @OneToMany(mappedBy = "reel")
//    @JsonManagedReference
//    private List<Comment> comments;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_Reels_likes",
            joinColumns = @JoinColumn(name = "reel_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @ToString.Exclude
    private Set<User> likedByUsers = new HashSet<>();
}
