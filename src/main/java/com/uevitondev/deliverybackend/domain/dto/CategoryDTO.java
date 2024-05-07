package com.uevitondev.deliverybackend.domain.dto;

import com.uevitondev.deliverybackend.domain.model.Category;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CategoryDTO implements Serializable {
    private UUID id;
    private String name;
    private final Set<ProductDTO> products = new HashSet<>();

    public CategoryDTO() {
    }

    public CategoryDTO(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public CategoryDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }

    public CategoryDTO(Category category, Set<Product> products) {
        this.id = category.getId();
        this.name = category.getName();
        products.forEach(product -> this.products.add(new ProductDTO(product)));
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ProductDTO> getProducts() {
        return products;
    }
}