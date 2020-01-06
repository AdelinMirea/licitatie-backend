package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.service;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.ServerLicitatieApplication;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Category;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.User;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto.CategoryDTO;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.CategoryRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.util.DTOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        ServerLicitatieApplication.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ControllerServiceTests {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private DTOUtils dtoUtils;

    private Category category1;
    private Category category2;

    @Before
    public void setUp() {
        Category category = new Category();
        category.setId(0);
        category.setUsers(new ArrayList<>());
        category.setAuctions(new ArrayList<>());
        category.setName("Bob");
        category1 = categoryRepository.save(category);
        category.setId(0);
        category.setName("Bill");
        category2 = categoryRepository.save(category);
    }

    @After
    public void tearDown() {
        categoryRepository.deleteAll();
    }

    @Test
    public void findAllCategories(){
        List<CategoryDTO> categoryDTOS = categoryService.findAllCategories();
        assertThat(categoryDTOS, containsInAnyOrder(dtoUtils.categoryToCategoryDTO(category1),
                dtoUtils.categoryToCategoryDTO(category2)));
    }
}
