package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.service;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.ServerLicitatieApplication;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Auction;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Category;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.User;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto.AuctionDTO;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.AuctionRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.CategoryRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.UserRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.util.DTOUtils;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.validator.TokenException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        ServerLicitatieApplication.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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

    private Auction auction1, auction2, auction3;
    private User user;
    private Category category1, category2, category3;

    @Before
    public void setUp() {
        category1 = new Category();
        category1 = categoryRepository.save(category1);
        category2 = new Category();
        category2 = categoryRepository.save(category2);
        category3 = new Category();
        category3 = categoryRepository.save(category3);
        user = new User();
        user.setCategories(Arrays.asList(category1, category2));
        user.setUserToken("1");
        user = userRepository.save(user);
        auction1 = createAuction("Title1", "", 2, true, user, category1);
        auction2 = createAuction("Title2", "Masina", 0, false, user, category2);
        auction3 = createAuction("Title3", "Flori", 0, false, user, category3);
    }

    @After
    public void tearDown() {
        auctionRepository.deleteAll();
    }

    public Auction createAuction(String title, String description, Integer minusDays, boolean closed, User user, Category category) {
        Auction auction = new Auction();
        auction.setTitle(title);
        auction.setDescription(description);
        auction.setOwner(user);
        auction.setCategory(category);
        auction.setBids(new ArrayList<>());
        auction.setClosed(closed);
        auction.setDateAdded(Date.valueOf(LocalDate.now().minusDays(minusDays)));
        auction.setImageNames(new ArrayList<>());

        return auctionService.save(auction);
    }
    @Test
    public void addAuctionDTO() throws Exception {
        Category category = new Category();
        category = categoryRepository.save(category);
        User user = new User();
        user.setId(9999);
        user = userRepository.save(user);
        AuctionDTO auctionDTO = new AuctionDTO();
        auctionDTO.setTitle("Titlu frum");
        auctionDTO.setDescription("Mama Yo Quero Una Aprobacion");
        auctionDTO.setOwnerId(user.getId());
        auctionDTO.setCategoryId(9999);
        auctionDTO.setBidsIds(new ArrayList<>());
        auctionDTO.setClosed(false);
        auctionDTO.setIsPrivate(false);
        auctionDTO.setStartingPrice(0.0);
        auctionDTO.setWinningBidId(1);
        auctionDTO.setDateAdded(Date.valueOf(LocalDate.now().minusDays(0)));
        assertThat(auctionDTO.getDescription(),is("Mama Yo Quero Una Aprobacion"));
        try {
            auctionService.save(auctionDTO);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void findAll() {
        assertThat(auctionService.findAll().size(), is(3));
    }

    @Test
    public void findAllSortedAndFiltered() {
        assertThat(auctionService.findAllSortedAndFiltered("dateAdded", "Masina", 0, 10).size(), is(1));
        assertThat(auctionService.findAllSortedAndFiltered("dateAdded", "", 0, 10).size(), is(3));
        assertThat(auctionService.findAllSortedAndFiltered("dateAdded", "", 0, 10).get(0), is(dtoUtils.auctionToAuctionDTO(auction3)));
    }

    @Test
    public void findAllActive() {
        assertThat(auctionService.findAllActive(0, 10).size(), is(2));
    }

    @Test
    public void findAuctionsByUserPreference() throws TokenException {
        List<AuctionDTO> auctionsDtos = auctionService.findAllActionsByUserPreferences(user.getUserToken(), 0, 2);
        assertThat(auctionsDtos, containsInAnyOrder(dtoUtils.auctionToAuctionDTO(auction1), dtoUtils.auctionToAuctionDTO(auction2)));
        List<AuctionDTO> auctionsDtos2 = auctionService.findAllActionsByUserPreferences(user.getUserToken(), 0, 1);
        assertThat(auctionsDtos2, hasSize(1));
        assertThat(Arrays.asList(dtoUtils.auctionToAuctionDTO(auction1), dtoUtils.auctionToAuctionDTO(auction2)), hasItem(auctionsDtos2.get(0)));
    }

}
