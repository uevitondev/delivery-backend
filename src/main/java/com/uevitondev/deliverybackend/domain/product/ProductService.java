package com.uevitondev.deliverybackend.domain.product;

import com.uevitondev.deliverybackend.config.aws.AwsS3Service;
import com.uevitondev.deliverybackend.domain.category.CategoryRepository;
import com.uevitondev.deliverybackend.domain.exception.DatabaseException;
import com.uevitondev.deliverybackend.domain.exception.ResourceNotFoundException;
import com.uevitondev.deliverybackend.domain.store.StoreRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final StoreRepository storeRepository;
    private final AwsS3Service awsS3Service;

    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            StoreRepository storeRepository, AwsS3Service awsS3Service
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.storeRepository = storeRepository;
        this.awsS3Service = awsS3Service;
    }

    public Product getProductById(UUID productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("product not found")
        );
    }

    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAllProducts(pageable).map(ProductDTO::new);
    }

    public Page<Product> findAllProductsByStoreId(UUID storeId, String productName, String categoryName, Pageable pageable) {
        return productRepository.findProductsByStoreIdWithFilters(storeId, productName, categoryName, pageable);
    }

    public ProductDTO findProductById(UUID productId) {
        return new ProductDTO(getProductById(productId));
    }

    @Transactional
    public ProductDTO insertNewProduct(MultipartFile file, NewProductDTO dto) throws IOException {
        var product = new Product();
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        product.setImgUrl(awsS3Service.uploadS3FileAndReturnUrl(file));
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setCategory(
                categoryRepository.findById(dto.categoryId())
                        .orElseThrow(() -> new ResourceNotFoundException("category not found"))
        );
        product.setStore(
                storeRepository.findById(dto.storeId())
                        .orElseThrow(() -> new ResourceNotFoundException("store not found"))
        );

        return new ProductDTO(productRepository.save(product));
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
