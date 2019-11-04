package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.service;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Auction;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Category;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto.AuctionDTO;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.AuctionRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.CategoryRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.util.DTOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
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
        List<Category> categories = categoryRepository.findAllByNameContaining(filter);
        List<Auction> auctions = auctionRepository.findAllByTitleContainingOrDescriptionContainingOrCategoryIn(filter, filter, categories, PageRequest.of(page , itemNumber, Sort.by(sortBy)));
        return auctions.parallelStream().map(dtoUtils::auctionToAuctionDTO).collect(Collectors.toList());
    }
}
