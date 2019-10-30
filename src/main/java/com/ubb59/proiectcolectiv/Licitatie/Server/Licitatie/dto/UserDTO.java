package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

public class UserDTO {
    @Getter
    @Setter
    private Integer id;
    @Getter
    @Setter
    private String firstName;
    @Getter
    @Setter
    private String lastName;
    @Getter
    @Setter
    private String mail;
    @Getter
    @Setter
    private Boolean verified;
    @Getter
    @Setter
    private Date lastActive;
    @Getter
    @Setter
    private Double rating;
    @Getter
    @Setter
    private Integer numberOfRatings;
    @Getter
    @Setter
    private Double numberOfCredits;
    @Getter
    @Setter
    private List<Integer> bidsIds;
    @Getter
    @Setter
    private List<Integer> auctionsIds;
    @Getter
    @Setter
    private List<Integer> commentsIds;
}
