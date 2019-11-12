package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class AuctionDTO {
    private Integer id;
    private String title;
    private String description;
    private Date dateAdded;
    private Boolean closed;
    private List<Integer> bidsIds;
    private Integer winningBidId;
    private Integer ownerId;
    private Integer categoryId;
}
