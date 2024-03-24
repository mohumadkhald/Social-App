package com.projects.socialapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class Message extends Base{
    private String content;
    private String img;

    private LocalDateTime timestamp;

    @ManyToOne
    private User user;

//    @JsonIgnore
    @ManyToOne
    private Chat chat;



}
