package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CommentDTO {
    private Integer id;
    private String content;
    private Date datePosted;
    private Integer userId;
    private String userName;
    private Integer postId;
}
