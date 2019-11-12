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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        ServerLicitatieApplication.class})
public class BidDTOTests {

    private BidDTO bidDTO;
    private Bid bid;
    private User user1;
    private User user2;
    private Auction auction1;
    private Auction auction2;

    @Autowired
    private DTOUtils dtoUtils;

    @Before
    public void before() {

        user1 = new User();
        user1.setId(1);
        user2 = new User();
        user2.setId(2);

        auction1 = new Auction();
        auction1.setId(1);
        auction2 = new Auction();
        auction2.setId(2);

        bid = new Bid();
        bid.setId(1);
        bid.setOffer((double) 1);
        bid.setBidder(user1);
        bid.setAuction(auction1);

        bidDTO = new BidDTO();
        bidDTO.setId(2);
        bidDTO.setOffer((double) 2);
        bidDTO.setBidderId(user2.getId());
        bidDTO.setAuctionId(auction2.getId());
    }

    @Test
    public void BidToBidDTO() {
        assertThat(bidDTO.getId(), is(2));
        assertThat(bidDTO.getOffer(), is((double) 2));
        assertThat(bidDTO.getBidderId(), is(2));
        assertThat(bidDTO.getAuctionId(), is(2));

        bidDTO = dtoUtils.bidToBidDTO(bid);

        assertThat(bidDTO.getId(), is(1));
        assertThat(bidDTO.getOffer(), is((double) 1));
        assertThat(bidDTO.getBidderId(), is(1));
        assertThat(bidDTO.getAuctionId(), is(1));
    }

    @Test
    public void BidDTOToBid() {
        assertThat(bid.getId(), is(1));
        assertThat(bid.getOffer(), is((double) 1));
        assertThat(bid.getBidder().getId(), is(1));
        assertThat(bid.getAuction().getId(), is(1));

        bidDTO.setOffer((double) 3);
        bid = dtoUtils.updateBidByBidDTO(bid, bidDTO, user2, auction2);

        assertThat(bid.getId(), is(1));
        assertThat(bid.getOffer(), is((double) 3));
        assertThat(bid.getBidder().getId(), is(2));
        assertThat(bid.getAuction().getId(), is(2));
    }

}
