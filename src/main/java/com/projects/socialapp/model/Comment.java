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
@EqualsAndHashCode(callSuper = true)
@Table(name = "comments")
public class Comment extends Base {
    private String content;
//    private String image;
//    private String video;
    @ManyToOne
    @JoinColumn(name="userId")
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name="postId")
    @JsonBackReference
    private Post post;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_comment_likes",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @ToString.Exclude
    private Set<User> likedByUsers = new HashSet<>();

}
