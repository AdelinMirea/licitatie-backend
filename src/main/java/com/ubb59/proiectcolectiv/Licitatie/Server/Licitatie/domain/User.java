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
    @Column(unique = true)
    private String mail;
    private String password;
    private String userToken;
    private Boolean verified;
    private Date lastActive;
    private Double rating;
    private Integer numberOfRatings;
    private Double numberOfCredits;
    private Boolean premium;
    private Integer noOfPrivateAuctions;
    private Boolean enabled;
    @ToString.Exclude
    @OneToMany
    private List<Bid> bids;
    @ToString.Exclude
    @OneToMany
    private List<Auction> auctions;
    @ToString.Exclude
    @OneToMany
    private List<Comment> comments;
    @ToString.Exclude
    @ManyToMany
    @JoinTable(name = "user_category",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id")})
    private List<Category> categories;
}
