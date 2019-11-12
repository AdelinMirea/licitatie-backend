package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BidDTO {
    private Integer id;
    private Double offer;
    private Integer bidderId;
    private Integer auctionId;
}
