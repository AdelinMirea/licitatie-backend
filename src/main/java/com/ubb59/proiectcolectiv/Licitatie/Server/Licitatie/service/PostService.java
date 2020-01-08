package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.service;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Auction;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Comment;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Post;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto.CommentDTO;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto.PostDTO;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.CommentRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.PostRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.util.DTOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Transactional
public class PostService {

    private PostRepository postRepository;
    private CommentRepository commentRepository;
    private DTOUtils dtoUtils;

    @Autowired
    PostService(PostRepository postRepository, CommentRepository commentRepository, DTOUtils dtoUtils) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.dtoUtils = dtoUtils;
    }

    /*
        Adds comment to an existing post
            - saves the comment into the database
            - then adds the comment to the comments list of the post
        @return the new post
        @throws EntityNotFoundException if there is no post with the given id in the database
     */
    public Post addComment(Integer postId, Comment comment) throws EntityNotFoundException {
        Post post = postRepository.getOne(postId);
        comment = commentRepository.save(comment);
        List<Comment> comments = post.getComments();
        comments.add(comment);
        post.setComments(comments);
        return postRepository.save(post);
    }

    public PostDTO getPostByAuctionId(Integer auctionId) {
        Optional<Post> postOptional = postRepository.findByAuction_Id(auctionId);
        if (postOptional.isPresent()) {
            return dtoUtils.postToPostDTO(postOptional.get());
        } else {
            throw new EntityNotFoundException("No auction with such id exists");
        }
    }

    public List<CommentDTO> getComments(Integer postId, Integer page, Integer itemNumber) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            List<Comment> comments = commentRepository.findByPost_IdOrderByDatePostedDesc(postId, PageRequest.of(page, itemNumber));
            return comments
                    .parallelStream()
                    .map(dtoUtils::commentToCommentDTO).collect(Collectors.toList());
        } else {
            throw new EntityNotFoundException("No post with such id exists");
        }
    }
}
