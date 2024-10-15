package com.uevitondev.deliverybackend.domain.category;

import com.uevitondev.deliverybackend.domain.exception.DatabaseException;
import com.uevitondev.deliverybackend.domain.exception.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    public Category getCategoryById(UUID categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("category not found")
        );
    }

    public List<CategoryDTO> findAllCategories() {
        return categoryRepository.findAll().stream().map(CategoryDTO::new).toList();
    }

    public CategoryDTO findCategoryById(UUID categoryId) {
        return new CategoryDTO(getCategoryById(categoryId));
    }

    @Transactional
    public CategoryDTO insertNewCategory(CategoryDTO dto) {
        var category = new Category(null, dto.name());
        return new CategoryDTO(categoryRepository.save(category));
    }

    @Transactional
    public CategoryDTO updateCategory(CategoryDTO dto) {
        var category = getCategoryById(dto.id());
        category.setName(dto.name());
        category.setUpdatedAt(LocalDateTime.now());
        return new CategoryDTO(categoryRepository.save(category));
    }

    @Transactional
    public void deleteCategoryById(UUID categoryId) {
        try {
            categoryRepository.deleteById(getCategoryById(categoryId).getId());
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("integrity constraint violation");
        }
    }

}
