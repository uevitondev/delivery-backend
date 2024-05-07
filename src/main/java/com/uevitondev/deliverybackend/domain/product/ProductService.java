package com.uevitondev.deliverybackend.domain.product;

import com.uevitondev.deliverybackend.domain.exception.DatabaseException;
import com.uevitondev.deliverybackend.domain.exception.ResourceNotFoundException;
import com.uevitondev.deliverybackend.domain.category.Category;
import com.uevitondev.deliverybackend.domain.store.Store;
import com.uevitondev.deliverybackend.domain.category.CategoryRepository;
import com.uevitondev.deliverybackend.domain.store.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final StoreRepository storeRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, StoreRepository storeRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.storeRepository = storeRepository;
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllProducts(Pageable pageable) {
        return productRepository.findAllProductsPaged(pageable).map(ProductDTO::new);
    }


    @Transactional(readOnly = true)
    public Page<ProductDTO> getAllProductsByStoreId(UUID id, Pageable pageable) {
        return productRepository.findAllProductsPagedByStoreId(id, pageable).map(ProductDTO::new);
    }


    @Transactional(readOnly = true)
    public ProductDTO findProductById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("product not found for productId: " + id));
        return new ProductDTO(product);
    }

    @Transactional
    public ProductDTO insertNewProduct(ProductDTO dto) {
        Product product = productRepository.save(convertProductDtoToProduct(dto));
        return new ProductDTO(product);
    }

    public Product convertProductDtoToProduct(ProductDTO dto) {

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("category not found for categoryId: " + dto.getCategoryId()));
        Store store = storeRepository.findById(dto.getStoreId())
                .orElseThrow(() -> new ResourceNotFoundException("store not found for storeId: " + dto.getStoreId()));

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setCategory(category);
        product.setStore(store);

        return product;
    }


    @Transactional
    public ProductDTO updateProductById(UUID id, ProductDTO dto) {
        try {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("category not found for categoryId: " + dto.getCategoryId()));
            Store store = storeRepository.findById(dto.getStoreId())
                    .orElseThrow(() -> new ResourceNotFoundException("store not found for storeId: " + dto.getStoreId()));


            Product product = productRepository.getReferenceById(id);
            product.setName(dto.getName());
            product.setDescription(dto.getDescription());
            product.setPrice(dto.getPrice());
            product.setCategory(category);
            product.setStore(store);
            product.setUpdateAt(LocalDateTime.now());
            product = productRepository.save(product);

            return new ProductDTO(product);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("product not found for productId: " + id);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteProductById(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("product not found for productId: " + id);
        }
        try {
            productRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Referential integrity constraint violation");
        }
    }


}
