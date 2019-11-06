package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
public class UserDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String mail;
    private Boolean verified;
    private Date lastActive;
    private Double rating;
    private Integer numberOfRatings;
    private Double numberOfCredits;
    private Boolean premium;
    private Integer noOfPrivateAuctions;
    private List<Integer> bidsIds;
    private List<Integer> auctionsIds;
    private List<Integer> commentsIds;
}
