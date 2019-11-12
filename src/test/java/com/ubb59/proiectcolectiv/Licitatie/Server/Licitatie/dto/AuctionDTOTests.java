package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.ServerLicitatieApplication;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.*;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.util.DTOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        ServerLicitatieApplication.class})
public class AuctionDTOTests {

    private Auction auction;
    private AuctionDTO auctionDTO;
    private Bid bid1;
    private Bid bid2;
    private User owner;
    private User owner2;
    private Category category;
    private Category category2;
    private List<Bid> bids;
    private List<Bid> bids2;

    @Autowired
    private DTOUtils dtoUtils;

    public AuctionDTOTests() {
    }

    @Before
    public void before() {
        bid1 = new Bid();
        bid1.setId(1);
        bid2 = new Bid();
        bid2.setId(2);
        owner = new User();
        owner.setId(1);
        owner2 = new User();
        owner2.setId(2);
        category = new Category();
        category.setId(1);
        category2 = new Category();
        category2.setId(2);

        bids = new ArrayList<>();
        bids.add(bid1);
        bids.add(bid2);

        bids2 = new ArrayList<>();
        bids2.add(bid2);

        auction = new Auction();
        auction.setId(1);
        auction.setTitle("Auction");
        auction.setClosed(true);
        auction.setDescription("Auction description");
        auction.setWinningBid(bid1);
        auction.setOwner(owner);
        auction.setCategory(category);
        auction.setBids(bids);

        auctionDTO = new AuctionDTO();
        auctionDTO.setId(2);
        auctionDTO.setTitle("AuctionDTO");
        auctionDTO.setDescription("AuctionDTO description");
        auctionDTO.setWinningBidId(2);
        auctionDTO.setClosed(false);
        auctionDTO.setOwnerId(2);
        auctionDTO.setCategoryId(2);
        auctionDTO.setBidsIds(Collections.singletonList(2));
    }

    @Test
    public void AuctionToAuctionDTO() {

        assertThat(auctionDTO.getId(), is(2));
        assertThat(auctionDTO.getTitle(), is("AuctionDTO"));
        assertThat(auctionDTO.getDescription(), is("AuctionDTO description"));
        assertThat(auctionDTO.getCategoryId(), is(2));
        assertThat(auctionDTO.getWinningBidId(), is(2));
        assertThat(auctionDTO.getOwnerId(), is(2));
        assertThat(auctionDTO.getBidsIds().size(), is(1));
        assertThat(auctionDTO.getClosed(), is(false));

        auctionDTO = dtoUtils.auctionToAuctionDTO(auction);

        assertThat(auctionDTO.getId(), is(1));
        assertThat(auctionDTO.getTitle(), is("Auction"));
        assertThat(auctionDTO.getDescription(), is("Auction description"));
        assertThat(auctionDTO.getCategoryId(), is(1));
        assertThat(auctionDTO.getWinningBidId(), is(1));
        assertThat(auctionDTO.getOwnerId(), is(1));
        assertThat(auctionDTO.getBidsIds().size(), is(2));
        assertThat(auctionDTO.getClosed(), is(true));

    }

    @Test
    public void AuctionDTOToAuction() {

        assertThat(auction.getId(), is(1));
        assertThat(auction.getTitle(), is("Auction"));
        assertThat(auction.getDescription(), is("Auction description"));
        assertThat(auction.getCategory().getId(), is(1));
        assertThat(auction.getWinningBid().getId(), is(1));
        assertThat(auction.getOwner().getId(), is(1));
        assertThat(auction.getBids().size(), is(2));
        assertThat(auction.getClosed(), is(true));

        auctionDTO.setClosed(false);
        auction = dtoUtils.updateAuctionByAuctionDTO(auction, auctionDTO, owner2, bid2, category2, bids2);

        assertThat(auction.getId(), is(1));
        assertThat(auction.getTitle(), is("AuctionDTO"));
        assertThat(auction.getDescription(), is("AuctionDTO description"));
        assertThat(auction.getCategory().getId(), is(2));
        assertThat(auction.getWinningBid().getId(), is(2));
        assertThat(auction.getOwner().getId(), is(2));
        assertThat(auction.getBids().size(), is(1));
        assertThat(auctionDTO.getClosed(), is(false));
    }
}
