package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.service;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Comment;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Post;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.CommentRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
public class PostService {

    private PostRepository postRepository;
    private CommentRepository commentRepository;

    @Autowired
    PostService(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
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
}
