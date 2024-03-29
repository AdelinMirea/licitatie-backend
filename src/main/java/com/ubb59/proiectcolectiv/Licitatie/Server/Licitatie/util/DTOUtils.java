package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.util;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.*;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto.*;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityExistsException;
import java.io.IOException;
import java.security.acl.Owner;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DTOUtils {

    private final UserRepository userRepository;
    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;
    private final CommentRepository commentRepository;
    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;

    @Autowired
    public DTOUtils(UserRepository userRepository,
                    BidRepository bidRepository,
                    AuctionRepository auctionRepository,
                    CommentRepository commentRepository,
                    CategoryRepository categoryRepository,
                    PostRepository postRepository) {
        this.userRepository = userRepository;
        this.bidRepository = bidRepository;
        this.auctionRepository = auctionRepository;
        this.commentRepository = commentRepository;
        this.categoryRepository = categoryRepository;
        this.postRepository = postRepository;
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
        userDTO.setEnable(user.getEnabled());
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
        List<Integer> categoriesId = user.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toList());
        userDTO.setCategoryIds(categoriesId);
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
                auctionRepository.findAllById(userDTO.getAuctionsIds()),
                commentRepository.findAllById(userDTO.getCommentsIds()), categoryRepository.findAllById(userDTO.getCategoryIds()));
    }

    public User updateUserByUserDTO(User user, UserDTO userDTO, List<Bid> bids, List<Auction> auctions, List<Comment> comments, List<Category> categories) {
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
        updatedUser.setEnabled(userDTO.getEnable());
        updatedUser.setNoOfPrivateAuctions(userDTO.getNoOfPrivateAuctions());
        updatedUser.setBids(bids);
        updatedUser.setAuctions(auctions);
        updatedUser.setComments(comments);
        updatedUser.setCategories(categories);
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
            user.setEnabled(false);
            //arbitrary date, we should know somehow that the user is new in the system
            user.setLastActive(Date.valueOf(LocalDate.of(2000, 1, 1)));
            user.setUserToken(token);
            user.setAuctions(new ArrayList<>());
            user.setBids(new ArrayList<>());
            user.setComments(new ArrayList<>());
            user.setCategories(new ArrayList<>());
            return user;
        }
    }

    public Auction auctionDTOToAuction(AuctionDTO auctionDTO) {
        Optional<Auction> auctionOptional = auctionRepository.findById(auctionDTO.getId());
        Auction auction = new Auction();
        if (auctionOptional.isPresent()) {
            auction = auctionOptional.get();
        }
        Optional<User> ownerOptional = userRepository.findById(auctionDTO.getOwnerId());
        Optional<Bid> winningBidOptional = bidRepository.findById(auctionDTO.getWinningBidId());
        Optional<Category> categoryOptional = categoryRepository.findById(auctionDTO.getCategoryId());
        List<Bid> bids = bidRepository.findAllById(auctionDTO.getBidsIds());
        Bid winningBid = winningBidOptional.orElse(null);
        Category category = categoryOptional.orElse(null);
        User owner = ownerOptional.orElse(null);
        auction = updateAuctionByAuctionDTO(auction, auctionDTO, owner, winningBid, category, bids);
        return auction;
    }

    public Auction updateAuctionByAuctionDTO(Auction auction, AuctionDTO auctionDTO, User owner, Bid winningBid, Category category, List<Bid> bids) {
        Auction updatedAuction = new Auction();
        if (auctionDTO.getDateAdded() == null) {
            updatedAuction.setDateAdded(Date.valueOf(LocalDate.now()));
        }
        updatedAuction.setDueDate(auction.getDueDate());
        updatedAuction.setId(auction.getId());
        updatedAuction.setTitle(auctionDTO.getTitle());
        updatedAuction.setClosed(auctionDTO.getClosed());
        updatedAuction.setDescription(auctionDTO.getDescription());
        updatedAuction.setStartingPrice(auctionDTO.getStartingPrice());
        updatedAuction.setIsPrivate(auctionDTO.getIsPrivate());
        updatedAuction.setOwner(owner);
        updatedAuction.setWinningBid(winningBid);
        updatedAuction.setCategory(category);
        updatedAuction.setBids(bids);
        updatedAuction.setImageNames(auction.getImageNames());
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
        auctionDTO.setDueDate(auction.getDueDate());
        auctionDTO.setOwnerId(auction.getOwner().getId());
        auctionDTO.setCategoryId(auction.getCategory().getId());
        auctionDTO.setStartingPrice(auction.getStartingPrice());
        auctionDTO.setIsPrivate(auction.getIsPrivate());
        if (auction.getWinningBid() != null) {
            auctionDTO.setWinningBidId(auction.getWinningBid().getId());
        }
        List<Integer> bidsIds = auction.getBids().stream()
                .map(Bid::getId)
                .collect(Collectors.toList());
        auctionDTO.setBidsIds(bidsIds);
        List<String> encodedImages = auction.getImageNames()
                .parallelStream()
                .map(imageName -> {
                    try {
                        return ImageUtils.getEncodedImageFromImageName(imageName);
                    } catch (IOException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        auctionDTO.setEncodedImages(encodedImages);
        return auctionDTO;
    }


    public Bid bidDTOToBid(BidDTO bidDTO) {
        Optional<Bid> bidOptional = bidRepository.findById(bidDTO.getId());
        Bid bid = new Bid();
        if (bidOptional.isPresent()) {
            bid = bidOptional.get();
        }
        User bidder = userRepository.getOne(bidDTO.getBidderId());
        Auction auction = auctionRepository.getOne(bidDTO.getAuctionId());
        bid = updateBidByBidDTO(bid, bidDTO, bidder, auction);
        return bid;
    }

    public Bid updateBidByBidDTO(Bid bid,
                                 BidDTO bidDTO,
                                 User bidder,
                                 Auction auction) {
        Bid updatedBid = new Bid();

        updatedBid.setId(bid.getId());
        updatedBid.setOffer(bidDTO.getOffer());
        updatedBid.setBidder(bidder);
        updatedBid.setAuction(auction);
        return updatedBid;
    }

    /**
     * Convert Bid to BidDTO
     */
    public BidDTO bidToBidDTO(Bid bid) {
        BidDTO bidDTO = new BidDTO();
        bidDTO.setId(bid.getId());
        bidDTO.setOffer(bid.getOffer());
        bidDTO.setBidderId(bid.getBidder().getId());
        bidDTO.setAuctionId(bid.getAuction().getId());

        return bidDTO;
    }

    public Comment commentDTOToComment(CommentDTO commentDTO) {
        Comment comment = new Comment();
        User owner = userRepository.getOne(commentDTO.getUserId());
        Post post = postRepository.getOne(commentDTO.getPostId());
        comment = updateCommentByCommentDTO(comment, commentDTO, owner, post);
        return comment;
    }

    public Comment updateCommentByCommentDTO(Comment comment,
                                             CommentDTO commentDTO,
                                             User owner,
                                             Post post) {
        Comment updatedComment = new Comment();
        if (commentDTO.getDatePosted() == null) {
            updatedComment.setDatePosted(Date.valueOf(LocalDate.now()));
        } else {
            updatedComment.setDatePosted(commentDTO.getDatePosted());
        }
        updatedComment.setId(comment.getId());
        updatedComment.setContent(commentDTO.getContent());
        updatedComment.setUser(owner);
        updatedComment.setPost(post);
        return updatedComment;
    }

    /**
     * Convert Comment to CommentDTO
     */
    public CommentDTO commentToCommentDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setDatePosted(comment.getDatePosted());
        commentDTO.setUserId(comment.getUser().getId());
        commentDTO.setUserName(comment.getUser().getFirstName() + " " + comment.getUser().getLastName());
        commentDTO.setPostId(comment.getPost().getId());
        return commentDTO;
    }

    public Post postDTOToPost(PostDTO postDTO) {
        Post post = postRepository.getOne(postDTO.getId());
        if (post == null) {
            return null;
        }
        Auction auction = auctionRepository.getOne(postDTO.getAuctionId());
        List<Comment> comments = new ArrayList<>(commentRepository.findAllById(postDTO.getCommentsIds()));
        post = updatePostByPostDTO(post, postDTO, auction, comments);
        return post;
    }

    public Post updatePostByPostDTO(Post post, PostDTO postDTO, Auction auction, List<Comment> comments) {
        post.setId(postDTO.getId());
        post.setAuction(auction);
        post.setComments(comments);
        return post;
    }


    public PostDTO postToPostDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setAuctionId(post.getAuction().getId());
        List<Integer> commentsIds = post.getComments().stream()
                .map(Comment::getId)
                .collect(Collectors.toList());
        postDTO.setCommentsIds(commentsIds);
        return postDTO;
    }

    public CategoryDTO categoryToCategoryDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        return categoryDTO;
    }

    public Category categoryDtoToCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());
        category.setAuctions(new ArrayList<>());
        category.setUsers(new ArrayList<>());
        return category;
    }
}
