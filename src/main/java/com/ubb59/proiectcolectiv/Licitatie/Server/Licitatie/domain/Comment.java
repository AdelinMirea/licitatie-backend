package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@ToString
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String content;
    private Date datePosted;
    @ManyToOne
    private User user;
    @ManyToOne
    private Post post;
}
