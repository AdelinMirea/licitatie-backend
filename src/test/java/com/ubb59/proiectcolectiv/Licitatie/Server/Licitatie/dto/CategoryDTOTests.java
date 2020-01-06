package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.ServerLicitatieApplication;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Category;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.util.DTOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        ServerLicitatieApplication.class})
public class CategoryDTOTests {

    @Autowired
    private DTOUtils dtoUtils;

    @Test
    public void categoryToCategoryDTO(){
        Category category = new Category();
        category.setId(1);
        category.setName("Bob");
        category.setUsers(new ArrayList<>());
        category.setAuctions(new ArrayList<>());
        CategoryDTO categoryDTO = dtoUtils.categoryToCategoryDTO(category);
        assertThat(categoryDTO.getId(), is(1));
        assertThat(categoryDTO.getName(), is("Bob"));
    }

    @Test
    public void categoryDtoToCategory(){
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1);
        categoryDTO.setName("Bob");
        Category category = dtoUtils.categoryDtoToCategory(categoryDTO);
        assertThat(category.getId(), is(1));
        assertThat(category.getName(), is("Bob"));
        assertThat(category.getAuctions().isEmpty(), is(true));
        assertThat(category.getUsers().isEmpty(), is(true));
    }
}
