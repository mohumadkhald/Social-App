package com.projects.socialapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;

    @ManyToOne
    private Chat chat;

    @Lob
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    private String img;
        // big dat
    //    @Lob
    //    @Column(name = "content", columnDefinition = "LONGTEXT")
    //    private String content;


    private String deletedByUser;


}
