package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.service;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Auction;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Category;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.User;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto.AuctionDTO;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.AuctionRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.CategoryRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.UserRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.util.DTOUtils;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.validator.DataValidationException;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.validator.TokenException;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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

    @Autowired
    public AuctionService(AuctionRepository auctionRepository, CategoryRepository categoryRepository, UserRepository userRepository, DTOUtils dtoUtils, Validator validator) {
        this.auctionRepository = auctionRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.dtoUtils = dtoUtils;
        this.validator = validator;
    }

    public List<Auction> findAll() {
        return this.auctionRepository.findAll();
    }

    public List<AuctionDTO> findAllSortedAndFiltered(String sortBy, String filter, Integer page, Integer itemNumber) {
        log.info("Entered method to get all auctions with parameters sortBy {}, filter {}, page {} and idemNumber {}", sortBy, filter, page, itemNumber);
        List<Category> categories = categoryRepository.findAllByNameContaining(filter);
        List<Auction> auctions = auctionRepository.findAllByTitleContainingOrDescriptionContainingOrCategoryIn(filter, filter, categories, PageRequest.of(page, itemNumber, Sort.by(Sort.Direction.DESC, sortBy)));
        return auctions.parallelStream().map(dtoUtils::auctionToAuctionDTO).collect(Collectors.toList());
    }

    public List<AuctionDTO> findAllActive() {
        return auctionRepository.findAllByClosed(false).parallelStream().map(dtoUtils::auctionToAuctionDTO).collect(Collectors.toList());
    }

    public List<AuctionDTO> findAllActionsByUserPreferences(String token) throws TokenException {
        Optional<User> user = userRepository.findByUserToken(token);
        if (!user.isPresent()) {
            throw new TokenException("Invalid token");
        } else {
            List<Auction> auctions = auctionRepository.findAllByCategoryIn(user.get().getCategories());
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
}
