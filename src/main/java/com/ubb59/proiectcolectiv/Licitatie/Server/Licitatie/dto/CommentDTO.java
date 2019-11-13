package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CommentDTO {
    private Integer id;
    private String content;
    private Date datePosted;
    private Integer userId;
    private Integer postId;
}
