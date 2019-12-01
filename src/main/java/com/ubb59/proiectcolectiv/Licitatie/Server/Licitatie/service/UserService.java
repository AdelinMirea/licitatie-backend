package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.service;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Auction;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Bid;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.User;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto.AuctionDTO;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.AuctionRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.BidRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.UserRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.validator.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserService {

    private UserRepository userRepository;
    private AuctionRepository auctionRepository;
    private BidRepository bidRepository;
    private Validator validator;

    @Autowired
    public UserService(UserRepository userRepository,
                       AuctionRepository auctionRepository,
                       BidRepository bidRepository,
                       Validator validator) {
        this.userRepository = userRepository;
        this.auctionRepository = auctionRepository;
        this.bidRepository = bidRepository;
        this.validator = validator;
    }

    public User addUser(User user) throws DataValidationException {
        if (!userRepository.findAllByMailEquals(user.getMail()).isEmpty()) {
            throw new EntityExistsException("User already exists");
        }
        validator.validateUser(user);
        String hashedPassword = DigestUtils.sha256Hex(user.getPassword());
        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

    public User getUserById(Integer id) throws DataValidationException {
        return userRepository.findById(id).orElseThrow(() -> new DataValidationException("User does not exist"));

    }

    public User updateUser(String token, String firstName, String lastName, String email) throws TokenException, DataValidationException {
        Optional<User> userOptional = userRepository.findByUserToken(token);
        if(!userOptional.isPresent()){
            throw new TokenException("Invalid token");
        }else {
            User updatedUser = userOptional.get();
            updatedUser.setFirstName(firstName);
            updatedUser.setLastName(lastName);
            updatedUser.setMail(email);
            validator.validateUser(updatedUser);
            userRepository.save(updatedUser);
            return updatedUser;
        }
    }

    public void updateUserPassword(String token, String oldPassword, String newPassword) throws TokenException, DataValidationException {
        Optional<User> userOptional = userRepository.findByUserToken(token);
        if(!userOptional.isPresent()){
            throw new TokenException("Invalid token");
        }else {
            User updatedUser = userOptional.get();
            String hashedPasswordOldPassword = DigestUtils.sha256Hex(oldPassword);
            if(!updatedUser.getPassword().equals(hashedPasswordOldPassword)){
                throw new DataValidationException("Invalid old password");
            }
            updatedUser.setPassword(newPassword);
            validator.validateUser(updatedUser);
            updatedUser.setPassword(DigestUtils.sha256Hex(newPassword));
            userRepository.save(updatedUser);
        }
    }

    public void updateUserRating(String token, User user, Double rating) throws TokenException, SameUserException {
        Optional<User> userOptional = userRepository.findByUserToken(token);
        if(!userOptional.isPresent()){
            throw new TokenException("Invalid token");
        }else {
            User givingRatingUser = userOptional.get();
            if(givingRatingUser.getId() == user.getId()){
                throw new SameUserException("User tries to give rating to himself.");
            }
            Integer oldNumberOfRatings = user.getNumberOfRatings();
            Integer newNumberOfRatings = oldNumberOfRatings + 1;
            Double oldRating = user.getRating();
            Double newRating = (oldRating * oldNumberOfRatings + rating) / newNumberOfRatings;

            user.setNumberOfRatings(newNumberOfRatings);
            user.setRating(newRating);

            userRepository.save(user);
        }
    }

    public String getUserTokenByCredentials(String email, String password) throws AuthenticationException {
        String hashedPassword = DigestUtils.sha256Hex(password);
        List<User> users = userRepository.findAllByMailEquals(email);
        if (users.isEmpty()) {
            throw new AuthenticationException("No user with such email exists");
        }
        User user = users.get(0);
        if (!user.getPassword().equals(hashedPassword)) {
            throw new AuthenticationException("Wrong password");
        }
        return user.getUserToken();
    }

    public List<Auction> getAuctionsCreatedByUser(Integer id) {
        Optional<User> user = userRepository.findById(id);

        if(user.isPresent()){
            return auctionRepository.findAllByOwner(user.get());
        }else{
            throw new EntityNotFoundException();
        }
    }

    public List<Auction> getAuctionsUserParticipated(Integer id) {
        Optional<User> user = userRepository.findById(id);

        if(user.isPresent()){
            List<Bid> bids = bidRepository.findAllByBidder(user.get());
            return bids.parallelStream().map(Bid::getAuction).distinct().collect(Collectors.toList());
        }else{
            throw new EntityNotFoundException();
        }
    }
}
