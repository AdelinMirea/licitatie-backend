package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.service;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.ServerLicitatieApplication;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Auction;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Comment;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Post;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto.PostDTO;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.AuctionRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.CommentRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.PostRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
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
}
