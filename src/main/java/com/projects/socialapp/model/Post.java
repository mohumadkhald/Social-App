package com.projects.socialapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"comments", "likedByUsers"})
@Table(name = "posts")
public class Post extends Base {
    private String caption;
    private String image;
    private String video;
    @ManyToOne
    @JoinColumn(name="userId")
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "post")
    @JsonManagedReference
    private List<Comment> comments;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_post_likes",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @ToString.Exclude
    private Set<User> likedByUsers = new HashSet<>();
}
