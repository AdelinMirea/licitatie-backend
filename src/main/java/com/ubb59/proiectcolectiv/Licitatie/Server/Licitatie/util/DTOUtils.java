package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.util;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.*;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto.AuctionDTO;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto.AuthenticationDTO;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto.UserDTO;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityExistsException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DTOUtils {

    private final UserRepository userRepository;
    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;
    private final CommentRepository commentRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public DTOUtils(UserRepository userRepository, BidRepository bidRepository, AuctionRepository auctionRepository, CommentRepository commentRepository, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.bidRepository = bidRepository;
        this.auctionRepository = auctionRepository;
        this.commentRepository = commentRepository;
        this.categoryRepository = categoryRepository;
    }

    public UserDTO userToUserDTO(User user) {
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
        userDTO.setPremium(user.getPremium());
        userDTO.setNoOfPrivateAuctions(user.getNoOfPrivateAuctions());
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
     * Finds user with the same id as userDTO, updates the other fields and returns it.
     * If there is no user with such id, returns @null.
     */
    public User userDTOToUser(UserDTO userDTO) {
        User user = userRepository.getOne(userDTO.getId());
        if (user == null) {
            return null;
        }
        return updateUserByUserDTO(user, userDTO, bidRepository.findAllById(userDTO.getBidsIds()),
                auctionRepository.findAllById(userDTO.getAuctionsIds()), commentRepository.findAllById(userDTO.getCommentsIds()));
    }

    public User updateUserByUserDTO(User user, UserDTO userDTO, List<Bid> bids, List<Auction> auctions, List<Comment> comments) {
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
        updatedUser.setPremium(userDTO.getPremium());
        updatedUser.setNoOfPrivateAuctions(userDTO.getNoOfPrivateAuctions());
        updatedUser.setBids(bids);
        updatedUser.setAuctions(auctions);
        updatedUser.setComments(comments);
        return updatedUser;
    }

    public User createUserFromAuthentication(AuthenticationDTO authenticationDTO, String token) {
        List<User> users = userRepository.findAllByMailEquals(authenticationDTO.getMail());
        if (!users.isEmpty()) {
            throw new EntityExistsException("A user with this e-mail address already exists");
        } else {
            User user = new User();
            user.setId(0);
            user.setFirstName(authenticationDTO.getFirstName());
            user.setLastName(authenticationDTO.getLastName());
            user.setPassword(authenticationDTO.getPassword());
            user.setMail(authenticationDTO.getMail());
            user.setVerified(false);
            user.setNumberOfCredits(0d);
            user.setRating(0d);
            user.setNumberOfRatings(0);
            user.setPremium(false);
            user.setNoOfPrivateAuctions(0);
            //arbitrary date, we should know somehow that the user is new in the system
            user.setLastActive(Date.valueOf(LocalDate.of(2000, 1, 1)));
            user.setUserToken(token);
            user.setAuctions(new ArrayList<>());
            user.setBids(new ArrayList<>());
            user.setComments(new ArrayList<>());
            return user;
        }
    }


    /**
     * Finds auction with the same id as auctionDTO, updates the other fields and returns it.
     * If there is no auction with such id, returns @null.
     */
    public Auction auctionDTOToAuction(AuctionDTO auctionDTO) {
        Auction auction = auctionRepository.getOne(auctionDTO.getId());
        if (auction == null) {
            return null;
        }
        User owner = userRepository.getOne(auctionDTO.getOwnerId());
        Bid winningBid = bidRepository.getOne(auctionDTO.getWinningBidId());
        Category category = categoryRepository.getOne(auctionDTO.getCategoryId());
        List<Bid> bids = bidRepository.findAllById(auctionDTO.getBidsIds());
        auction = updateAuctionByAuctionDTO(auction, auctionDTO, owner, winningBid, category, bids);
        return auction;
    }

    public Auction updateAuctionByAuctionDTO(Auction auction, AuctionDTO auctionDTO, User owner, Bid winningBid, Category category, List<Bid> bids) {
        Auction updatedAuction = new Auction();
        if(auctionDTO.getDateAdded() == null){
            updatedAuction.setDateAdded(Date.valueOf(LocalDate.now()));
        }
        updatedAuction.setId(auction.getId());
        updatedAuction.setTitle(auctionDTO.getTitle());
        updatedAuction.setClosed(auctionDTO.getClosed());
        updatedAuction.setDescription(auctionDTO.getDescription());
        updatedAuction.setOwner(owner);
        updatedAuction.setWinningBid(winningBid);
        updatedAuction.setCategory(category);
        updatedAuction.setBids(bids);
        return updatedAuction;
    }

    /**
     * Convert auction to AuctionDTO
     */
    public AuctionDTO auctionToAuctionDTO(Auction auction) {
        AuctionDTO auctionDTO = new AuctionDTO();
        auctionDTO.setId(auction.getId());
        auctionDTO.setTitle(auction.getTitle());
        auctionDTO.setClosed(auction.getClosed());
        auctionDTO.setDescription(auction.getDescription());
        auctionDTO.setDateAdded(auction.getDateAdded());
        auctionDTO.setOwnerId(auction.getOwner().getId());
        auctionDTO.setCategoryId(auction.getCategory().getId());
        if(auction.getWinningBid() != null){
            auctionDTO.setWinningBidId(auction.getWinningBid().getId());
        }
        List<Integer> bidsIds = auction.getBids().stream()
                .map(Bid::getId)
                .collect(Collectors.toList());
        auctionDTO.setBidsIds(bidsIds);
        return auctionDTO;
    }

}
