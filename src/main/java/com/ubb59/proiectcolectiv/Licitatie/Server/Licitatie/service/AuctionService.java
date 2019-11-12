package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.service;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Auction;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Category;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto.AuctionDTO;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.AuctionRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.CategoryRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.util.DTOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class AuctionService {

    private AuctionRepository auctionRepository;
    private CategoryRepository categoryRepository;
    private DTOUtils dtoUtils;

    @Autowired
    public AuctionService(AuctionRepository auctionRepository, CategoryRepository categoryRepository, DTOUtils dtoUtils){
        this.auctionRepository = auctionRepository;
        this.categoryRepository = categoryRepository;
        this.dtoUtils = dtoUtils;
    }

    public List<Auction> findAll(){
        return this.auctionRepository.findAll();
    }

    public List<AuctionDTO> findAllSortedAndFiltered(String sortBy, String filter, Integer page, Integer itemNumber) {
        log.info("Entered method to get all auctions with parameters sortBy {}, filter {}, page {} and idemNumber {}", sortBy, filter, page, itemNumber);
        List<Category> categories = categoryRepository.findAllByNameContaining(filter);
        List<Auction> auctions = auctionRepository.findAllByTitleContainingOrDescriptionContainingOrCategoryIn(filter, filter, categories, PageRequest.of(page , itemNumber, Sort.by(Sort.Direction.DESC, sortBy)));
        return auctions.parallelStream().map(dtoUtils::auctionToAuctionDTO).collect(Collectors.toList());
    }
    public List<AuctionDTO>findAllActive(){
      return   auctionRepository.findAllByClosed(false).parallelStream().map(dtoUtils::auctionToAuctionDTO).collect(Collectors.toList());
    }
    public Auction save(Auction auction) {
        return auctionRepository.save(auction);
    }

    public Auction save(AuctionDTO auction) {
        Auction newAuction = dtoUtils.auctionDTOToAuction(auction);
        return save(newAuction);
    }
}
