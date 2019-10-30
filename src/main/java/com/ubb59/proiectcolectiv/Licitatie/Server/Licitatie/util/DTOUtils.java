package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.util;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Auction;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Bid;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Comment;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.User;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto.UserDTO;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.AuctionRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.BidRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.CommentRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DTOUtils {

    private final UserRepository userRepository;
    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public DTOUtils(UserRepository userRepository, BidRepository bidRepository, AuctionRepository auctionRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.bidRepository = bidRepository;
        this.auctionRepository = auctionRepository;
        this.commentRepository = commentRepository;
    }

    public UserDTO userToUserDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setLastActive(user.getLastActive());
        userDTO.setMail(user.getMail());
        userDTO.setRating(user.getRating());
        userDTO.setNumberOfRatings(user.getNumberOfRatings());
        userDTO.setNumberOfCredits(user.getNumberOfCredits());
        userDTO.setVerified(user.getVerified());
        List<Integer> bidsIds = user.getBids().stream()
                .map(Bid::getId)
                .collect(Collectors.toList());
        userDTO.setBidsIds(bidsIds);
        List<Integer> auctionIds = user.getAuctions().stream()
                .map(Auction::getId)
                .collect(Collectors.toList());
        userDTO.setAuctionsIds(auctionIds);
        List<Integer> commentsIds = user.getComments().stream()
                .map(Comment::getId)
                .collect(Collectors.toList());
        userDTO.setCommentsIds(commentsIds);
        return userDTO;
    }

    /**
    Finds user with the same id as userDTO, updates the other fields and returns it.
    If there is no user with such id, returns @null.
    */
    public User userDTOToUser(UserDTO userDTO){
        User user = userRepository.getOne(userDTO.getId());
        if(user == null){
            return null;
        }
        return updateUserByUserDTO(user, userDTO, bidRepository.findAllById(userDTO.getBidsIds()),
                auctionRepository.findAllById(userDTO.getAuctionsIds()), commentRepository.findAllById(userDTO.getCommentsIds()));
    }

    public User updateUserByUserDTO(User user, UserDTO userDTO, List<Bid> bids, List<Auction> auctions, List<Comment> comments){
        User updatedUser = new User();
        updatedUser.setPassword(user.getPassword());
        updatedUser.setUserToken(user.getUserToken());
        updatedUser.setId(user.getId());
        updatedUser.setFirstName(userDTO.getFirstName());
        updatedUser.setLastName(userDTO.getLastName());
        updatedUser.setLastActive(userDTO.getLastActive());
        updatedUser.setMail(userDTO.getMail());
        updatedUser.setRating(userDTO.getRating());
        updatedUser.setNumberOfRatings(userDTO.getNumberOfRatings());
        updatedUser.setNumberOfCredits(userDTO.getNumberOfCredits());
        updatedUser.setVerified(userDTO.getVerified());
        updatedUser.setBids(bids);
        updatedUser.setAuctions(auctions);
        updatedUser.setComments(comments);
        return updatedUser;
    }
}
