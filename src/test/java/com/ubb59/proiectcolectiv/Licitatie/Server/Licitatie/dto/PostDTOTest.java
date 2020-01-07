package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.ServerLicitatieApplication;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Auction;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Comment;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Post;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.util.DTOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        ServerLicitatieApplication.class})
public class PostDTOTest {

    private Post post;
    private PostDTO postDTO;
    private Auction auction1;
    private Auction auction2;
    private List<Comment> comments1;
    private List<Comment> comments2;

    @Autowired
    private DTOUtils dtoUtils;


    @Before
    public void before() {
        auction1 = new Auction();
        auction1.setId(1);
        auction2 = new Auction();
        auction2.setId(2);
        Comment comment1 = new Comment();
        comment1.setId(1);
        comment1.setContent("Comment 1");
        comments1 = new ArrayList<>();
        comments1.add(comment1);

        post = new Post();
        post.setId(1);
        post.setAuction(auction1);
        post.setComments(comments1);

        postDTO = new PostDTO();
        postDTO.setId(2);
        postDTO.setId(2);
        postDTO.setAuctionId(2);
        postDTO.setCommentsIds(Arrays.asList(1, 2));
        comments2 = new ArrayList<>();
    }

    @Test
    public void PostToPostDTO() {

        assertThat(postDTO.getId(), is(2));
        assertThat(postDTO.getAuctionId(), is(2));
        assertThat(postDTO.getCommentsIds().size(), is(2));

        postDTO = dtoUtils.postToPostDTO(post);

        assertThat(postDTO.getId(), is(1));
        assertThat(postDTO.getAuctionId(), is(1));
        assertThat(postDTO.getCommentsIds().size(), is(1));
    }

    @Test
    public void updatePostByPostDTO() {

        assertThat(post.getId(), is(1));
        assertThat(post.getAuction().getId(), is(1));
        assertThat(post.getComments().size(), is(1));

        post = dtoUtils.updatePostByPostDTO(post, postDTO, auction2, comments2);

        assertThat(post.getId(), is(2));
        assertThat(post.getAuction().getId(), is(2));
        assertThat(post.getComments().size(), is(0));
    }

}
