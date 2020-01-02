package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@EqualsAndHashCode
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String description;
    private Date dateAdded;
    private Timestamp dueDate;
    private Boolean closed;
    @ColumnDefault(value = "0.0")
    private Double startingPrice;
    private Boolean isPrivate;
    @ToString.Exclude
    @OneToMany(fetch = FetchType.EAGER)
    private List<Bid> bids;
    @OneToOne
    private Bid winningBid;
    @ManyToOne
    private User owner;
    @ManyToOne
    private Category category;
    @ElementCollection
    @CollectionTable(name = "ImageNames", joinColumns = @JoinColumn(name = "auction_id"))
    @Column(name = "imageName")
    private Collection<String> imageNames;
}
