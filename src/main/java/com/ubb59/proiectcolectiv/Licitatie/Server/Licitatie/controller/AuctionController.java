package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.controller;


import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto.AuctionDTO;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto.BidDTO;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.service.AuctionService;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.validator.DataValidationException;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.validator.TokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@CrossOrigin(
        origins = {"*"}
)
public class AuctionController {

    private AuctionService auctionService;

    @Autowired
    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @GetMapping("/auctions/{id}")
    public ResponseEntity<?> getAuction(@PathVariable Integer id){
        try {
            return new ResponseEntity<>(this.auctionService.findAuction(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
    public ResponseEntity<?> getAllActiveAuctions(@RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
                                                  @RequestParam(name = "itemNumber", defaultValue = "10", required = false) Integer itemNumber
    ) {
        List<AuctionDTO> auctions = auctionService.findAllActive(page, itemNumber);
        return new ResponseEntity<>(auctions, HttpStatus.OK);
    }

    @GetMapping("/auctions/byPreferences")
    public ResponseEntity<?> getAuctionsByPreferences(@RequestHeader("token") String token,
                                                      @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
                                                      @RequestParam(name = "itemNumber", defaultValue = "10", required = false) Integer itemNumber) {
        try {
            List<AuctionDTO> auctions = auctionService.findAllActionsByUserPreferences(token, page, itemNumber);
            return new ResponseEntity<>(auctions, HttpStatus.OK);
        } catch (TokenException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/auctions")
    public ResponseEntity<?> add(
            @RequestBody AuctionDTO auctionDTO
    ) {
        try {
            auctionService.save(auctionDTO);
        } catch (DataValidationException e) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (EntityExistsException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(auctionDTO, HttpStatus.OK);
    }

    @GetMapping("/auctions/edge")
    public ResponseEntity<?> getEndingAuctions(@RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
                                               @RequestParam(name = "itemNumber", defaultValue = "10", required = false) Integer itemNumber) {
        List<AuctionDTO> auctions = auctionService.findAllEndingPaginated(page,itemNumber);
        return new ResponseEntity<>(auctions, HttpStatus.OK);
    }

    @PostMapping("/auctions/end/{id}")
    public ResponseEntity<AuctionDTO> endAuction(@PathVariable Integer id) {
        try {
            return new ResponseEntity<AuctionDTO>(auctionService.endAuction(id), HttpStatus.OK);
        } catch (DataValidationException e) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/auctions/bid")
    public ResponseEntity<?> addBid(
            @RequestBody BidDTO bidDTO
    ) {
        try {
            auctionService.saveBid(bidDTO);
        } catch (EntityExistsException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(bidDTO, HttpStatus.OK);
    }
    @GetMapping({"/auction/bids/{auctionId}"})
    public ResponseEntity<List<BidDTO>> getBidsByAuctionId(@PathVariable Integer auctionId) {
        try {
            AuctionDTO auction=auctionService.findAuction(auctionId);
            List<BidDTO>bidDTOS=auctionService.getBidsByAuction(auction);
            return new ResponseEntity<>(bidDTOS, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
