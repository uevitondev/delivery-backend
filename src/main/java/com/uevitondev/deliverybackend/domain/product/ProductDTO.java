package com.uevitondev.deliverybackend.domain.product;

import java.io.Serializable;
import java.util.UUID;

public class ProductDTO implements Serializable {
    private UUID id;
    private String name;
    private String imgUrl;
    private String description;
    private Double price;

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.imgUrl = product.getImgUrl();
        this.description = product.getDescription();
        this.price = product.getPrice();
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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
