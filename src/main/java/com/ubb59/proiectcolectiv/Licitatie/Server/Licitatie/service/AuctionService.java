package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.service;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Auction;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Bid;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Category;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.User;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto.AuctionDTO;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.event.OnEndAuctionNotificationEvent;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.event.OnRegistrationSuccessEvent;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.AuctionRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.CategoryRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.UserRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.util.DTOUtils;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.validator.DataValidationException;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.validator.TokenException;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Transactional
@Slf4j
public class AuctionService {

    private AuctionRepository auctionRepository;
    private CategoryRepository categoryRepository;
    private UserRepository userRepository;
    private DTOUtils dtoUtils;
    private Validator validator;
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    public AuctionService(AuctionRepository auctionRepository, CategoryRepository categoryRepository, UserRepository userRepository, DTOUtils dtoUtils, Validator validator, ApplicationEventPublisher eventPublisher) {
        this.auctionRepository = auctionRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.dtoUtils = dtoUtils;
        this.validator = validator;
        this.eventPublisher = eventPublisher;
    }

    public List<Auction> findAll() {
        return this.auctionRepository.findAll();
    }

    public List<AuctionDTO> findAllSortedAndFiltered(String sortBy, String filter, Integer page, Integer itemNumber) {
        log.info("Entered method to get all auctions with parameters sortBy {}, filter {}, page {} and idemNumber {}", sortBy, filter, page, itemNumber);
        List<Category> categories = categoryRepository.findAllByNameContaining(filter);
        List<Auction> auctions = auctionRepository.findAllByTitleContainingOrDescriptionContainingOrCategoryIn(filter, filter, categories, PageRequest.of(page, itemNumber, Sort.by(Sort.Direction.ASC, sortBy)));
        return auctions.parallelStream().map(dtoUtils::auctionToAuctionDTO).collect(Collectors.toList());
    }

    public List<AuctionDTO> findAllActive(Integer page, Integer itemNumber) {
        return auctionRepository.findAllByClosed(false, PageRequest.of(page, itemNumber)).parallelStream().map(dtoUtils::auctionToAuctionDTO).collect(Collectors.toList());
    }
    public List<AuctionDTO> findAllActive() {
        return auctionRepository.findAllByClosed(false).parallelStream().map(dtoUtils::auctionToAuctionDTO).collect(Collectors.toList());
    }

    public List<AuctionDTO> findAllActionsByUserPreferences(String token, Integer page, Integer itemNumber) throws TokenException {
        Optional<User> user = userRepository.findByUserToken(token);
        if (!user.isPresent()) {
            throw new TokenException("Invalid token");
        } else {
            List<Auction> auctions = auctionRepository.findAllByCategoryIn(user.get().getCategories(), PageRequest.of(page, itemNumber));
            return auctions
                    .parallelStream()
                    .map(auction -> dtoUtils.auctionToAuctionDTO(auction))
                    .collect(Collectors.toList());
        }
    }

    public Auction save(Auction auction) {
        return auctionRepository.save(auction);
    }

    public Auction save(AuctionDTO auction) throws DataValidationException {
        Auction newAuction = dtoUtils.auctionDTOToAuction(auction);
        validator.validateAuction(newAuction);
        return save(newAuction);
    }

    public Auction closeAction(Auction action) {
        action.setClosed(true);
        return auctionRepository.save(action);
    }

    public List<Auction> closeAuctionsWithDueDatePassed() {
        List<Auction> closedAuctionList = new ArrayList<>();
        auctionRepository.findAllByClosed(false).forEach(auction -> {
            if (auction.getDueDate().before(new Timestamp(System.currentTimeMillis()))) {
                Optional<Bid> winningBid = auction.getBids()
                        .stream()
                        .min((bid1, bid2) -> bid2.getOffer().compareTo(bid1.getOffer()));
                if(winningBid.isPresent()){
                    auction.setWinningBid(winningBid.get());
                    eventPublisher.publishEvent(new OnEndAuctionNotificationEvent(auction.getOwner(), auction));
                }
                auction.getBids().forEach(bid -> eventPublisher.publishEvent(new OnEndAuctionNotificationEvent(bid.getBidder(), auction)));
                closedAuctionList.add(this.closeAction(auction));
            }
        });

        return closedAuctionList;
    }

    public List<AuctionDTO> findAllEnding() {
        Timestamp timestamp1 = new Timestamp(new Date().getTime());
        Timestamp timestamp2 = new Timestamp(new Date().getTime());
        int duration = 24*60*60*1000;
        timestamp2.setTime(timestamp2.getTime() + duration);
        List<Auction> auctions = auctionRepository.findByDueDateBetween(timestamp1, timestamp2);

        return auctions
                .parallelStream()
                .map(auction -> dtoUtils.auctionToAuctionDTO(auction))
                .collect(Collectors.toList());
    }

    public AuctionDTO endAuction(Integer id) throws EntityNotFoundException, DataValidationException {
        Auction auction = auctionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Auction does not exist"));
        if(auction.getClosed()){
            throw new DataValidationException("Auction already closed");
        }
        auction.setClosed(true);
        return dtoUtils.auctionToAuctionDTO(auctionRepository.save(auction));
    }
}
