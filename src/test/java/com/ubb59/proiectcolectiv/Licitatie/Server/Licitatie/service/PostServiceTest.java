package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.service;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.ServerLicitatieApplication;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Auction;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Comment;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Post;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.User;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto.CommentDTO;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto.PostDTO;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.AuctionRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.CommentRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.PostRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.UserRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.util.DTOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        ServerLicitatieApplication.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PostServiceTest {

    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private AuctionRepository auctionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DTOUtils dtoUtils;

    private Post post;
    private Comment comment;
    private Auction auction;

    @Before
    public void setup() {
        auction = new Auction();
        auction.setId(0);
        auction = auctionRepository.save(auction);
        post = new Post();
        post.setAuction(auction);
        post.setComments(new ArrayList<>());
        postRepository.save(post);

        comment = new Comment();
        comment.setContent("hello");
    }

    @Test
    public void addCommentSuccess() {
        Post addedPost = postService.addComment(post.getId(), comment);
        assertThat(commentRepository.findAll().size(), is(1));
        assertThat(addedPost.getComments().size(), is(1));
    }

    @Test(expected = EntityNotFoundException.class)
    public void addCommentFailure() {
        postService.addComment(2, comment);
    }

    @Test
    public void getPostByAuctionId() {
        PostDTO postDTO = postService.getPostByAuctionId(auction.getId());
        assertThat(postDTO.getId(), is(post.getId()));
        assertThat(postDTO.getAuctionId(), is(auction.getId()));
    }

    @Test
    public void getComments() {
        User user = new User();
        user.setId(0);
        user = userRepository.save(user);
        Comment comment1 = new Comment();
        comment1.setId(0);
        comment1.setDatePosted(new Date(3));
        comment1.setUser(user);
        comment1.setPost(post);
        comment1 = commentRepository.save(comment1);
        Comment comment2 = new Comment();
        comment2.setId(0);
        comment2.setDatePosted(new Date(1));
        comment2.setUser(user);
        comment2.setPost(post);
        comment2 = commentRepository.save(comment2);
        Comment comment3 = new Comment();
        comment3.setId(0);
        comment3.setDatePosted(new Date(2));
        comment3.setUser(user);
        comment3.setPost(post);
        comment3 = commentRepository.save(comment3);

        //needed for the date to be in the proper format
        comment1 = commentRepository.findById(comment1.getId()).get();
        comment2 = commentRepository.findById(comment2.getId()).get();
        comment3 = commentRepository.findById(comment3.getId()).get();

        post.setComments(Arrays.asList(comment1, comment2, comment3));
        post = postRepository.save(post);
        List<CommentDTO> commentsDTOs = postService.getComments(post.getId(), 0, 2);
        assertThat(commentsDTOs, containsInAnyOrder(dtoUtils.commentToCommentDTO(comment1), dtoUtils.commentToCommentDTO(comment3)));

    }
}
