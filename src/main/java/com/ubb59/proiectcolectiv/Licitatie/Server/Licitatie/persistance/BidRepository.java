package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Auction;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Bid;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface BidRepository extends JpaRepository<Bid, Integer> {
    List<Bid> findAllByBidder(User bidder);
    List<Bid> findAllByAuction(Auction auction);
}
