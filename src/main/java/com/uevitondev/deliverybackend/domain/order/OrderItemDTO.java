package com.uevitondev.deliverybackend.domain.order;

import com.uevitondev.deliverybackend.domain.product.ProductDTO;
import com.uevitondev.deliverybackend.domain.orderitem.OrderItem;

import java.io.Serializable;
import java.util.UUID;

public class OrderItemDTO implements Serializable {
    private UUID id;
    private Integer quantity;
    private Double total;
    private String observation;
    private ProductDTO product;

    public OrderItemDTO() {
    }

    public OrderItemDTO(UUID id, Integer quantity, Double total, String observation, ProductDTO product) {
        this.id = id;
        this.quantity = quantity;
        this.total = total;
        this.observation = observation;
        this.product = product;
    }

    public OrderItemDTO(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.quantity = orderItem.getQuantity();
        this.total = orderItem.getTotal();
        this.observation = orderItem.getObservation();
        this.product = new ProductDTO(orderItem.getProduct());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }
}
