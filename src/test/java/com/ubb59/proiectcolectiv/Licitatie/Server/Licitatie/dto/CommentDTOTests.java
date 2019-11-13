package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.ServerLicitatieApplication;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Comment;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Post;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.User;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.util.DTOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        ServerLicitatieApplication.class})
public class CommentDTOTests {

    private CommentDTO commentDTO;
    private Comment comment;
    private User user1;
    private User user2;
    private Post post1;
    private Post post2;
    private Date date1 = Calendar.getInstance().getTime();
    private Date date2 = Calendar.getInstance().getTime();

    @Autowired
    private DTOUtils dtoUtils;

    public CommentDTOTests() {
    }

    @Before
    public void before() {

        user1 = new User();
        user1.setId(1);
        user2 = new User();
        user2.setId(2);

        post1 = new Post();
        post1.setId(1);
        post2 = new Post();
        post2.setId(2);

        comment = new Comment();
        comment.setId(1);
        comment.setContent("Content1");
        comment.setDatePosted(date1);
        comment.setUser(user1);
        comment.setPost(post1);

        commentDTO = new CommentDTO();
        commentDTO.setId(2);
        commentDTO.setContent("Content2");
        commentDTO.setDatePosted(date2);
        commentDTO.setUserId(user2.getId());
        commentDTO.setPostId(post2.getId());
    }

    @Test
    public void CommentToCommentDTO() {
        assertThat(commentDTO.getId(), is(2));
        assertThat(commentDTO.getContent(), is("Content2"));
        assertThat(commentDTO.getDatePosted(), is(date2));
        assertThat(commentDTO.getUserId(), is(2));
        assertThat(commentDTO.getPostId(), is(2));

        commentDTO = dtoUtils.commentToCommentDTO(comment);

        assertThat(commentDTO.getId(), is(1));
        assertThat(commentDTO.getContent(), is("Content1"));
        assertThat(commentDTO.getDatePosted(), is(date1));
        assertThat(commentDTO.getUserId(), is(1));
        assertThat(commentDTO.getPostId(), is(1));
    }

    @Test
    public void CommentDTOToComment() {
        assertThat(comment.getId(), is(1));
        assertThat(comment.getContent(), is("Content1"));
        assertThat(comment.getDatePosted(), is(date1));
        assertThat(comment.getUser().getId(), is(1));
        assertThat(comment.getPost().getId(), is(1));

        commentDTO.setContent("Content3");
        comment = dtoUtils.updateCommentByCommentDTO(comment, commentDTO, user2, post2);

        assertThat(comment.getId(), is(1));
        assertThat(comment.getContent(), is("Content3"));
        assertThat(comment.getDatePosted(), is(date2));
        assertThat(comment.getUser().getId(), is(2));
        assertThat(comment.getPost().getId(), is(2));
    }
}
