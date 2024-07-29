package com.uevitondev.deliverybackend.domain.product;

import java.io.Serializable;
import java.util.UUID;

public class ProductDTO implements Serializable {
    private UUID id;
    private String imgUrl;
    private String name;
    private String description;
    private Double price;

    public ProductDTO(Product product) {
        this.imgUrl = product.getImgUrl();
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
