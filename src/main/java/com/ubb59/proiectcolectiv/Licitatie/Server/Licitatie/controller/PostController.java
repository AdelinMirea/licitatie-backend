package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.controller;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Comment;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto.CommentDTO;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.service.PostService;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.util.DTOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(
        origins = {"*"}
)
public class PostController {

    private PostService postService;
    private DTOUtils dtoUtils;

    @Autowired
    PostController(PostService postService, DTOUtils dtoUtils) {
        this.postService = postService;
        this.dtoUtils = dtoUtils;
    }

    @PostMapping({"/posts/comments/{postId}"})
    public ResponseEntity<String> addUser(@RequestBody CommentDTO commentDTO, @PathVariable Integer postId) {
        Comment comment = dtoUtils.commentDTOToComment(commentDTO);
        try {
            postService.addComment(postId, comment);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
