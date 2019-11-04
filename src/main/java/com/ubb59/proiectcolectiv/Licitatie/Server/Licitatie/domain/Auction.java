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
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String description;
    private Date dateAdded;
    @ToString.Exclude
    @OneToMany
    private List<Bid> bids;
    @OneToOne
    private Bid winningBid;
    @ManyToOne
    private User owner;
    @ManyToOne
    private Category category;
}
