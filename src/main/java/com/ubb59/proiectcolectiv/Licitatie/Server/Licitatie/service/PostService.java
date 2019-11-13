package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.service;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Comment;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Post;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostService {

    private PostRepository postRepository;

    @Autowired
    PostService(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    public Post addComment(Integer postId, Comment comment) throws Exception{
        Post post = postRepository.getOne(postId);
        if(post == null){
            throw new Exception("Post not found");
        }
        List<Comment> comments = post.getComments();
        comments.add(comment);
        post.setComments(comments);
        return postRepository.save(post);
    }
}
