package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.service;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.ServerLicitatieApplication;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Comment;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Post;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.PostRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

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

    private Post post;
    private Comment comment;

    @Before
    public void setup() {
        post = new Post();
        post.setId(1);
        post.setComments(new ArrayList<>());
//        postRepository.save(post);

        comment = new Comment();
        comment.setContent("hello");
    }

    @Test
    public void addCommentSuccess() throws Exception {
        postRepository.save(post);
        Post addedPost = postService.addComment(1, comment);
        assertThat(addedPost.getComments().size(), is(1));
        assertThat(addedPost.getComments().get(0).getContent(), is("Hello"));
    }
//
//    @Test(expected = Exception.class)
//    public void addCommentFailure() throws Exception {
//        postService.addComment(2, comment);
//    }

}
