package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.service;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto.CategoryDTO;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.CategoryRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.util.DTOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional
public class CategoryService {

    private CategoryRepository categoryRepository;
    private DTOUtils dtoUtils;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, DTOUtils dtoUtils) {
        this.categoryRepository = categoryRepository;
        this.dtoUtils = dtoUtils;
    }

    public List<CategoryDTO> findAllCategories(){
        return categoryRepository.findAll()
                .parallelStream()
                .map(dtoUtils::categoryToCategoryDTO)
                .collect(Collectors.toList());
    }
}
