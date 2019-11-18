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
            @RequestParam(name = "auctionDTO") AuctionDTO auctionDTO
    ){
        auctionService.save(auctionDTO);
        return auctionDTO;
    }

}
