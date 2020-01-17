package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
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
    private Timestamp dueDate;
    private Boolean closed;
    private Double startingPrice;
    private Boolean isPrivate;
    private List<Integer> bidsIds;
    private Integer winningBidId;
    private Integer ownerId;
    private Integer categoryId;
    private List<String> encodedImages;
    private MultipartFile[] images;
}
