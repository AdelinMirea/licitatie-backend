package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.service;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.ServerLicitatieApplication;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Auction;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Category;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.User;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.AuctionRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.CategoryRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.UserRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.util.DTOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        ServerLicitatieApplication.class})
public class AuctionServiceTest {


    @Autowired
    private AuctionService auctionService;
    @Autowired
    private DTOUtils dtoUtils;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AuctionRepository auctionRepository;
    @Autowired
    private UserRepository userRepository;

    private Auction auction1, auction2;
    @Before
    public void setUp() {
        auction1 = createAuction("Title1", "", 2);
        auction2 = createAuction("Title2", "Masina", 0);
    }

    @After
    public void tearDown(){
        auctionRepository.deleteAll();
    }

    public Auction createAuction(String title, String description, Integer minusDays){
        Category category = new Category();
        category = categoryRepository.save(category);
        User user = new User();
        user = userRepository.save(user);

        Auction auction = new Auction();
        auction.setTitle(title);
        auction.setDescription(description);
        auction.setOwner(user);
        auction.setCategory(category);
        auction.setBids(new ArrayList<>());
        auction.setDateAdded(Date.valueOf(LocalDate.now().minusDays(minusDays)));
        return auctionService.save(auction);
    }

    @Test
    public void findAll() {
        assertThat(auctionService.findAll().size(), is(2));
    }

    @Test
    public void findAllSortedAndFiltered() {
        assertThat(auctionService.findAllSortedAndFiltered("dateAdded", "Masina", 0, 10).size(), is(1));
        assertThat(auctionService.findAllSortedAndFiltered("dateAdded", "", 0, 10).size(), is(2));
        assertThat(auctionService.findAllSortedAndFiltered("dateAdded", "", 0, 10).get(0), is(dtoUtils.auctionToAuctionDTO(auction2)));
    }
}