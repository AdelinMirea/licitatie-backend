package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ToString.Exclude
    @OneToMany(fetch = FetchType.EAGER,
            cascade=CascadeType.ALL)
    private Set<Comment> comments;
    @OneToOne
    private Auction auction;
}
