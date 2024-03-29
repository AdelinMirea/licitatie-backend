package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Auction;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Category;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

@Repository
@Transactional
public interface AuctionRepository extends JpaRepository<Auction, Integer> {
    List<Auction> findAllByTitleContainingOrDescriptionContainingOrCategoryIn(String title, String descriprion, List<Category> category, Pageable of);
    List<Auction> findAllByClosed(Boolean closed, Pageable of);
    List<Auction> findAllByClosed(Boolean closed);
    List<Auction> findAllByOwner(User owner);
    List<Auction> findAllByCategoryIn(List<Category> categories);
    List<Auction> findAllByCategoryIn(List<Category> categories, Pageable of);
    List<Auction> findByDueDateBetween(Timestamp timestamp1, Timestamp timestamp2);
    List<Auction> findByDueDateBetween(Timestamp timestamp1, Timestamp timestamp2,Pageable of);
}
