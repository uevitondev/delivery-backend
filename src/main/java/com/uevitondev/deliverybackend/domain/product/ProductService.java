package com.uevitondev.deliverybackend.domain.product;

import com.uevitondev.deliverybackend.domain.category.CategoryRepository;
import com.uevitondev.deliverybackend.domain.exception.DatabaseException;
import com.uevitondev.deliverybackend.domain.exception.ResourceNotFoundException;
import com.uevitondev.deliverybackend.domain.store.StoreRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final StoreRepository storeRepository;

    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            StoreRepository storeRepository
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.storeRepository = storeRepository;
    }

    public Product getProductById(UUID productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("product not found")
        );
    }

    public Page<ProductDTO> getAllProductsPaged(Pageable pageable) {
        return productRepository.findAllProductsPaged(pageable).map(ProductDTO::new);
    }

    public Page<ProductDTO> getAllProductsPagedByStoreAndCategory(
            UUID storeId,
            String categoryName,
            Pageable pageable
    ) {
        return productRepository.findAllByStoreAndCategory(storeId, categoryName, pageable).map(ProductDTO::new);
    }

    public ProductDTO findProductById(UUID productId) {
        return new ProductDTO(getProductById(productId));
    }

    @Transactional
    public ProductDTO insertNewProduct(NewProductDTO dto) {
        var product = productRepository.save(convertNewProductDtoToProduct(dto));
        return new ProductDTO(product);
    }

    public Product convertNewProductDtoToProduct(NewProductDTO dto) {
        var category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("category not found"));
        var store = storeRepository.findById(dto.storeId())
                .orElseThrow(() -> new ResourceNotFoundException("store not found"));
        var product = new Product();
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setCategory(category);
        product.setStore(store);
        return product;
    }


    @Transactional
    public ProductDTO updateProduct(ProductDTO dto) {
        var product = getProductById(dto.id());
        product.setImgUrl(dto.imgUrl());
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setUpdatedAt(LocalDateTime.now());
        return new ProductDTO(productRepository.save(product));
    }

    @Transactional
    public void deleteProductById(UUID productId) {
        try {
            productRepository.deleteById(getProductById(productId).getId());
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("integrity constraint violation");
        }
    }


}
