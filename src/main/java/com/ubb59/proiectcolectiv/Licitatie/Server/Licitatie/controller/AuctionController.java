package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.controller;


import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Auction;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto.AuctionDTO;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.service.AuctionService;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.util.DTOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import java.util.List;

@RestController
@CrossOrigin(
        origins = {"*"}
)
public class AuctionController {

    private AuctionService auctionService;
    private DTOUtils dtoUtils;

    @Autowired
    public AuctionController(AuctionService auctionService, DTOUtils dtoUtils) {
        this.auctionService = auctionService;
        this.dtoUtils = dtoUtils;
    }

    @PostMapping("/auctions/add")
    public ResponseEntity<AuctionDTO> addAuction(@RequestBody AuctionDTO auctionDTO) {
        try {

            Auction auction = auctionService.save(dtoUtils.auctionDTOToAuction(auctionDTO));
            return new ResponseEntity<>(dtoUtils.auctionToAuctionDTO(auction), HttpStatus.OK);
        } catch (EntityExistsException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/auctions")
    public ResponseEntity<?> getAllAuctions(
            @RequestParam(name = "sortBy", defaultValue = "dateAdded", required = false) String sortBy,
            @RequestParam(name = "filter", defaultValue = "", required = false) String filter,
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "itemNumber", defaultValue = "10", required = false) Integer itemNumber
    ) {
        List<AuctionDTO> auctions = auctionService.findAllSortedAndFiltered(sortBy, filter, page, itemNumber);
        return new ResponseEntity<>(auctions, HttpStatus.OK);
    }

    @GetMapping("/auctions/now")
    public ResponseEntity<?> getAllActiveAuctions() {
        List<AuctionDTO> auctions = auctionService.findAllActive();
        return new ResponseEntity<>(auctions, HttpStatus.OK);

    }

}
