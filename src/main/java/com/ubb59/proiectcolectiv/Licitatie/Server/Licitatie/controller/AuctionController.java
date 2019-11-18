package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.controller;


import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Auction;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto.AuctionDTO;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.service.AuctionService;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.util.DTOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(
        origins = {"*"}
)
public class AuctionController {

    private AuctionService auctionService;
    private DTOUtils dtoUtils;

    @Autowired
    public AuctionController(AuctionService auctionService){
        this.auctionService = auctionService;
    }

    @GetMapping("/auctions")
    public ResponseEntity<?> getAllAuctions(
            @RequestParam(name = "sortBy", defaultValue = "dateAdded", required = false) String sortBy,
            @RequestParam(name = "filter", defaultValue = "", required = false) String filter,
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "itemNumber", defaultValue = "10", required = false) Integer itemNumber
    ){
        List<AuctionDTO> auctions = auctionService.findAllSortedAndFiltered(sortBy, filter, page, itemNumber);
        return new ResponseEntity<>(auctions, HttpStatus.OK);
    }
    @GetMapping("/auctions/now")
    public ResponseEntity<?>getAllActiveAuctions(){
        List<AuctionDTO> auctions = auctionService.findAllActive();
        return new ResponseEntity<>(auctions, HttpStatus.OK);

    }

    @PostMapping("/auctions")
    public AuctionDTO add(
            @RequestParam(name = "id") Integer id,
            @RequestParam(name = "title") String title,
            @RequestParam(name = "description") String description,
            @RequestParam(name = "dateAdded") Date dateAdded,
            @RequestParam(name = "closed") Boolean closed,
            @RequestParam(name = "startingPrice") Double startingPrice,
            @RequestParam(name = "isPrivate") Boolean isPrivate,
            @RequestParam(name = "bidsIds") List<Integer> bidsIds,
            @RequestParam(name = "winningBidId") Integer winningBidId,
            @RequestParam(name = "ownerId") Integer ownerId,
            @RequestParam(name = "categoryId") Integer categoryId
    ){
        Auction auction = new Auction(id,title,description,dateAdded,closed,startingPrice,isPrivate,bidsIds,winningBidId,ownerId,categoryId);
        AuctionDTO auctionDTO = dtoUtils.auctionToAuctionDTO(auction);
        auctionService.save(auctionDTO);
        return auctionDTO;
    }

}
