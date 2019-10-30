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
    @Getter
    @Setter
    private Integer id;
    @Getter
    @Setter
    private String firstName;
    @Getter
    @Setter
    private String lastName;
    @Column(unique = true)
    @Getter
    @Setter
    private String mail;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private String userToken;
    @Getter
    @Setter
    private Boolean verified;
    @Getter
    @Setter
    private Date lastActive;
    @Getter
    @Setter
    private Double rating;
    @Getter
    @Setter
    private Integer numberOfRatings;
    @Getter
    @Setter
    private Double numberOfCredits;
    @ToString.Exclude
    @OneToMany
    @Getter
    @Setter
    private List<Bid> bids;
    @ToString.Exclude
    @OneToMany
    @Getter
    @Setter
    private List<Auction> auctions;
    @ToString.Exclude
    @OneToMany
    @Getter
    @Setter
    private List<Comment> comments;
}
