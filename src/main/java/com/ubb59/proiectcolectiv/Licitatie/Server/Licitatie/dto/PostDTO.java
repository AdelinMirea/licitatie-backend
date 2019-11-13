package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostDTO {
    private Integer id;
    private List<Integer> commentsIds;
    private Integer auctionId;
}
