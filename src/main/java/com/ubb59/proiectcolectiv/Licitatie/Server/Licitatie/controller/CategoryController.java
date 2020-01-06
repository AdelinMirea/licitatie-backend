package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.controller;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto.CategoryDTO;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(
        origins = {"*"}
)
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping({"/categories"})
    public ResponseEntity<List<CategoryDTO>> addComment() {
            List<CategoryDTO> categoryDTOS = categoryService.findAllCategories();
            return new ResponseEntity<>(categoryDTOS, HttpStatus.OK);
    }

}
