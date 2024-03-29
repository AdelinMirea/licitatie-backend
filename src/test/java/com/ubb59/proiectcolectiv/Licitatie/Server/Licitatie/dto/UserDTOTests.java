package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.ServerLicitatieApplication;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.*;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.util.DTOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        ServerLicitatieApplication.class})
public class UserDTOTests {

    private User user;
    private UserDTO userDto;
    private Auction auction;
    private Comment comment;
    private Bid bid;
    private Category category;
    @Autowired
    private DTOUtils dtoUtils;

    @Before
    public void setupUser() {
        auction = new Auction();
        auction.setId(2);
        comment = new Comment();
        comment.setId(3);
        bid = new Bid();
        bid.setId(4);
        category = new Category();
        category.setId(5);
        user = new User();
        user.setId(1);
        user.setUserToken("1");
        user.setPassword("1");
        user.setFirstName("a");
        user.setLastName("a");
        user.setMail("a");
        user.setNumberOfCredits(1.0);
        user.setNumberOfRatings(1);
        user.setLastActive(new Date(1));
        user.setRating(1.0);
        user.setVerified(true);
        user.setPremium(false);
        user.setEnabled(false);
        user.setNoOfPrivateAuctions(0);
        user.setAuctions(new ArrayList<>(Collections.singletonList(auction)));
        user.setBids(new ArrayList<>(Collections.singletonList(bid)));
        user.setComments(new ArrayList<>(Collections.singletonList(comment)));
        user.setCategories(new ArrayList<>(Collections.singletonList(category)));
    }

    private void setupUserDTO() {
        userDto = new UserDTO();
        userDto.setId(1);
        userDto.setFirstName("b");
        userDto.setLastName("b");
        userDto.setMail("b");
        userDto.setNumberOfCredits(2.0);
        userDto.setNumberOfRatings(2);
        userDto.setLastActive(new Date(2));
        userDto.setRating(2.0);
        userDto.setVerified(false);
        userDto.setPremium(true);
        userDto.setEnable(true);
        userDto.setNoOfPrivateAuctions(2);
        userDto.setAuctionsIds(new ArrayList<>(Collections.singletonList(2)));
        userDto.setBidsIds(new ArrayList<>(Collections.singletonList(3)));
        userDto.setCommentsIds(new ArrayList<>(Collections.singletonList(4)));
        userDto.setCategoryIds(new ArrayList<>(Collections.singletonList(5)));
    }

    @Test
    public void userToUserDTO() {
        UserDTO userDTO = dtoUtils.userToUserDTO(user);
        assertThat(userDTO.getId(), is(1));
        assertThat(userDTO.getFirstName(), is("a"));
        assertThat(userDTO.getLastName(), is("a"));
        assertThat(userDTO.getMail(), is("a"));
        assertThat(userDTO.getNumberOfCredits(), is(1.0));
        assertThat(userDTO.getNumberOfRatings(), is(1));
        assertThat(userDTO.getLastActive(), is(new Date(1)));
        assertThat(userDTO.getRating(), is(1.0));
        assertThat(userDTO.getVerified(), is(true));
        assertThat(userDTO.getPremium(), is(false));
        assertThat(userDTO.getEnable(), is(false));
        assertThat(userDTO.getNoOfPrivateAuctions(), is(0));
        assertThat(userDTO.getAuctionsIds(), containsInAnyOrder(auction.getId()));
        assertThat(userDTO.getBidsIds(), containsInAnyOrder(bid.getId()));
        assertThat(userDTO.getCommentsIds(), containsInAnyOrder(comment.getId()));
        assertThat(userDTO.getCategoryIds(), containsInAnyOrder(category.getId()));
    }

    @Test
    public void updateUserByUserDTO() {
        setupUserDTO();
        User newUser = dtoUtils.updateUserByUserDTO(user, userDto, Collections.singletonList(bid),
                Collections.singletonList(auction), Collections.singletonList(comment), Collections.singletonList(category));
        assertThat(newUser.getId(), is(1));
        assertThat(newUser.getPassword(), is("1"));
        assertThat(newUser.getUserToken(), is("1"));
        assertThat(newUser.getFirstName(), is("b"));
        assertThat(newUser.getLastName(), is("b"));
        assertThat(newUser.getMail(), is("b"));
        assertThat(newUser.getNumberOfCredits(), is(2.0));
        assertThat(newUser.getNumberOfRatings(), is(2));
        assertThat(newUser.getLastActive(), is(new Date(2)));
        assertThat(newUser.getRating(), is(2.0));
        assertThat(newUser.getVerified(), is(false));
        assertThat(newUser.getPremium(), is(true));
        assertThat(newUser.getEnabled(), is(true));
        assertThat(newUser.getNoOfPrivateAuctions(), is(2));
        assertThat(newUser.getAuctions(), containsInAnyOrder(auction));
        assertThat(newUser.getBids(), containsInAnyOrder(bid));
        assertThat(newUser.getComments(), containsInAnyOrder(comment));
        assertThat(newUser.getCategories(), containsInAnyOrder(category));
    }
}
