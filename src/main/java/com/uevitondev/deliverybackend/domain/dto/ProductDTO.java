package com.uevitondev.deliverybackend.domain.dto;

import org.antlr.v4.runtime.misc.NotNull;

import java.io.Serializable;
import java.util.UUID;

public class ProductDTO implements Serializable {
    private UUID id;

    @NotBlank(message = "invalid: name is empty")
    @NotNull(message = "invalid: name is null")
    private String name;
    private String imageUrl;
    @NotBlank(message = "invalid: description is empty")
    @NotNull(message = "invalid: description is null")
    private String description;

    @NotNull(message = "invalid: price is null")
    private Double price;

    @NotNull(message = "invalid: categoryId is null")
    private UUID categoryId;

    @NotNull(message = "invalid: storeId is null")
    private UUID storeId;

    public ProductDTO() {
    }

    public ProductDTO(UUID id, String name, String imageUrl, String description, Double price, UUID categoryId, UUID storeId) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
        this.storeId = storeId;
    }

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.imageUrl = product.getImageUrl();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.categoryId = product.getCategory().getId();
        this.storeId = product.getStore().getId();
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    public UUID getStoreId() {
        return storeId;
    }

    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }
}
