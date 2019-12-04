package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.service;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.ServerLicitatieApplication;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Auction;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Bid;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Comment;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.User;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.AuctionRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.BidRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.CommentRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.UserRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.validator.AuthenticationException;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.validator.DataValidationException;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.validator.SameUserException;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.validator.TokenException;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityExistsException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        ServerLicitatieApplication.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserServiceTests {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuctionRepository auctionRepository;
    @Autowired
    private BidRepository bidRepository;
    @Autowired
    private CommentRepository commentRepository;

    private User user1;
    private User user2;

    public void createAuctions(User aux){
        Auction auction = new Auction();
        auction.setOwner(aux);
        auctionRepository.save(auction);
        Auction auction2 = new Auction();
        auction2.setOwner(aux);
        auctionRepository.save(auction2);

        Bid bid = new Bid();
        bid.setAuction(auction);
        bid.setBidder(aux);
        bidRepository.save(bid);
    }


    public void createComments(User aux){
        Comment comment = new Comment();
        comment.setUser(aux);
        commentRepository.save(comment);
        Comment comment2 = new Comment();
        comment2.setUser(aux);
        commentRepository.save(comment2);
    }

    @Before
    public void setupUsers() {
        user1 = new User();
        user1.setUserToken("1");
        user1.setPassword("123456");
        user1.setFirstName("a");
        user1.setLastName("a");
        user1.setMail("a@a.com");
        user1.setNumberOfCredits(1.0);
        user1.setNumberOfRatings(1);
        user1.setLastActive(new Date(1));
        user1.setRating(1.0);
        user1.setVerified(true);
        user1.setEnabled(false);
        user1.setAuctions(new ArrayList<>());
        user1.setBids(new ArrayList<>());
        user1.setComments(new ArrayList<>());
        user2 = new User();
        user2.setUserToken("2");
        user2.setPassword("123456");
        user2.setFirstName("a");
        user2.setLastName("a");
        user2.setMail("b@b.com");
        user2.setNumberOfCredits(1.0);
        user2.setNumberOfRatings(1);
        user2.setLastActive(new Date(1));
        user2.setRating(1.0);
        user2.setVerified(true);
        user2.setAuctions(new ArrayList<>());
        user2.setBids(new ArrayList<>());
        user2.setComments(new ArrayList<>());
    }

    @Test
    public void addUser() throws DataValidationException {
        User addedUser = userService.addUser(user1);
        assertThat(addedUser.getMail(), is(user1.getMail()));
        User addedUser2 = userService.addUser(user2);
        assertThat(addedUser2.getMail(), is(user2.getMail()));
    }

    @Test(expected = EntityExistsException.class)
    public void addUserAlreadyExists() throws DataValidationException {
        userService.addUser(user1);
        user2.setMail(user1.getMail());
        userService.addUser(user2);
    }

    @Test(expected = DataValidationException.class)
    public void addUserNullData() throws DataValidationException {
        user1.setRating(null);
        userService.addUser(user1);
    }

    @Test(expected = DataValidationException.class)
    public void addUserInvalidDataName() throws DataValidationException {
        user1.setFirstName("");
        user1.setLastName("");
        userService.addUser(user1);
    }

    @Test(expected = DataValidationException.class)
    public void addUserInvalidPassword() throws DataValidationException {
        user1.setPassword("12345");
        userService.addUser(user1);
    }

    @Test
    public void getUserTokenByCredentials() throws DataValidationException, AuthenticationException {
        String password = user1.getPassword();
        userService.addUser(user1);
        String token = userService.getUserTokenByCredentials(user1.getMail(), password);
        assertThat(token, is(user1.getUserToken()));
    }

    @Test(expected = AuthenticationException.class)
    public void getUserTokenByCredentialsUserNotFound() throws DataValidationException, AuthenticationException {
        String password = user1.getPassword();
        userService.addUser(user1);
        userService.getUserTokenByCredentials(user1.getMail() + "wrong", password);
    }

    @Test(expected = AuthenticationException.class)
    public void getUserTokenByCredentialsWrongPassword() throws DataValidationException, AuthenticationException {
        String password = user1.getPassword();
        userService.addUser(user1);
        userService.getUserTokenByCredentials(user1.getMail(), password + "wrong");
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void updateUser() throws DataValidationException, TokenException {
        userService.addUser(user1);
        userService.updateUser(user1.getUserToken(), "b", "b", "b@b.com");
        Optional<User> updatedUser = userRepository.findByUserToken(user1.getUserToken());
        assertThat(updatedUser.isPresent(), is(true));
        assertThat(updatedUser.get().getMail(), is("b@b.com"));
        assertThat(updatedUser.get().getFirstName(), is("b"));
        assertThat(updatedUser.get().getLastName(), is("b"));
    }

    @Test(expected = TokenException.class)
    public void updateUserInvalidToken() throws DataValidationException, TokenException {
        userService.addUser(user1);
        userService.updateUser("invalid_token", "b", "b", "b@b.com");
    }

    @Test(expected = DataValidationException.class)
    public void updateUserInvalidData() throws DataValidationException, TokenException {
        userService.addUser(user1);
        userService.updateUser(user1.getUserToken(), "", "", "");
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void updateUserPassword() throws DataValidationException, TokenException {
        userService.addUser(user1);
        userService.updateUserPassword(user1.getUserToken(), "123456", "aaaaaa");
        Optional<User> updatedUser = userRepository.findByUserToken(user1.getUserToken());
        assertThat(updatedUser.isPresent(), is(true));
        assertThat(updatedUser.get().getPassword(), is(DigestUtils.sha256Hex("aaaaaa")));
    }

    @Test(expected = TokenException.class)
    public void updateUserPasswordInvalidToken() throws DataValidationException, TokenException {
        userService.addUser(user1);
        userService.updateUserPassword("invalid_token", "123456", "aaaaaa");
    }

    @Test(expected = DataValidationException.class)
    public void updateUserPasswordInvalidData() throws DataValidationException, TokenException {
        userService.addUser(user1);
        userService.updateUserPassword(user1.getUserToken(), "123456", "aaaaa");
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void updateUserRating() throws TokenException, SameUserException, DataValidationException {
        userService.addUser(user1);
        userService.addUser(user2);

        Integer newNumberOfRatings = user2.getNumberOfRatings() + 1;

        userService.updateUserRating(user1.getUserToken(), user2, 4.0);

        Optional<User> updatedUser = userRepository.findByUserToken(user2.getUserToken());
        assertThat(updatedUser.isPresent(), is(true));
        assertThat(updatedUser.get().getNumberOfRatings(), is(newNumberOfRatings));
        assertThat(updatedUser.get().getRating(), is(2.5));
    }

    @Test(expected = TokenException.class)
    public void updateUserRatingInvalidToken() throws DataValidationException, TokenException, SameUserException {
        userService.addUser(user1);
        userService.addUser(user2);
        userService.updateUserRating("invalid_token", user2, 1.0);
    }

    @Test(expected = SameUserException.class)
    public void updateUserRatingUserGivesRatingToHimself() throws DataValidationException, TokenException, SameUserException {
        userService.addUser(user1);
        userService.updateUserRating("1", user1, 1.0);
    }

    @Test
    public void enableRegisteredUser()  {
        userService.enableRegisteredUser(user1);
    }

    @Test
    public void getAuctionsCreatedByUser() {
        User aux = userRepository.save(user1);
        createAuctions(aux);
        assertThat(userService.getAuctionsCreatedByUser(aux.getId()).size(), is(2));
    }

    @Test
    public void getAuctionsUserParticipated() {
        User aux = userRepository.save(user1);
        createAuctions(aux);
        assertThat(userService.getAuctionsUserParticipated(aux.getId()).size(), is(1));
    }

    @Test
    public void getCommentsCreatedByUser() {
        User aux = userRepository.save(user1);
        createComments(aux);
        assertThat(userService.getCommentsCreatedByUser(aux.getId()).size(), is(2));
    }

    @Test
    public void getUserByToken() throws DataValidationException {
        userService.addUser(user1);
        User user = userService.getUserByToken("1");
        assertThat(user.getLastName(), is("a"));
    }

    @Test(expected = DataValidationException.class)
    public void getUserByTokenException() throws DataValidationException {
        User user = userService.getUserByToken("3");
        assertThat(user.getLastName(), is("a"));
    }
}
