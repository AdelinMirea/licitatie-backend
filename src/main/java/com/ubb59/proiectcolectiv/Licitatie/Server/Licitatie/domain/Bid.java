package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Double offer;
    @ManyToOne
    private User bidder;
    @ManyToOne
    private Auction auction;
}
