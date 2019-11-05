package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.service;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Auction;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class AuctionServiceTest {


    @Autowired
    private AuctionService auctionService;

    @Before
    public void setUp() throws Exception {
        Auction auction = new Auction();
        auction.setTitle("Title1");
        auctionService.save(auction);
    }

    @Test
    public void findAll() {
    }

    @Test
    public void findAllSortedAndFiltered() {
    }
}