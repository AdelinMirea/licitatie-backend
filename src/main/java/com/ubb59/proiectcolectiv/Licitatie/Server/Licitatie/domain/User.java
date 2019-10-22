package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String lastName;
    private String mail;
    private String password;
    private Boolean verified;
    private Date lastActive;
    private Integer rating;
    @ToString.Exclude
    @OneToMany
    private List<Bid> bids;
    @ToString.Exclude
    @OneToMany
    private List<Auction> auctions;
    @ToString.Exclude
    @OneToMany
    private List<Comment> comments;
}
