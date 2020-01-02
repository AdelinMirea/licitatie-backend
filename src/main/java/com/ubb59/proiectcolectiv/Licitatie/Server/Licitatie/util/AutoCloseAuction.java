package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.util;


import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.service.AuctionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class AutoCloseAuction {
    private AuctionService auctionService;
   @Autowired
    public AutoCloseAuction(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @Autowired

    @Scheduled(cron="0 0/15 * * * *")
    public void autoCloseAuctions(){
        log.info("Auto job for closing auctions runs");
        auctionService.closeAuctionsWithDueDatePassed();
    }


}
