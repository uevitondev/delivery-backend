package com.uevitondev.deliverybackend.domain.category;

import com.uevitondev.deliverybackend.domain.exception.DatabaseException;
import com.uevitondev.deliverybackend.domain.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDTO> findAllCategories() {
        return categoryRepository.findAll().stream().map(CategoryDTO::new).toList();
    }

    public CategoryDTO findCategoryById(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("category not found for categoryId: " + id));
        return new CategoryDTO(category);
    }

    public CategoryDTO insertNewCategory(CategoryDTO dto) {
        Category category = new Category(null,dto.getName());
        category = categoryRepository.save(category);

        return new CategoryDTO(category);
    }

    public CategoryDTO updateCategoryById(UUID id, CategoryDTO dto) {
        try {
            Category category = categoryRepository.getReferenceById(id);
            category.setName(dto.getName());
            category.setUpdatedAt(LocalDateTime.now());
            category = categoryRepository.save(category);
            return new CategoryDTO(category);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("category not found for categoryId: " + id);
        }
    }

    public void deleteCategoryById(UUID id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("category not found for categoryId: " + id);
        }
        try {
            categoryRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Referential integrity constraint violation");
        }
    }
}
