package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class AuctionDTO {
    private Integer id;
    private String title;
    private String description;
    private Date dateAdded;
    private List<Integer> bidsIds;
    private Integer winningBidId;
    private Integer ownerId;
    private Integer categoryId;
}
