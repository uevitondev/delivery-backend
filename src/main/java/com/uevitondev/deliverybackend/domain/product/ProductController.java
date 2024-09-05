package com.uevitondev.deliverybackend.domain.product;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<PagedModel<ProductDTO>> getAllProductsPaged(Pageable pageable) {
        return ResponseEntity.ok().body(
                new PagedModel<>(productService.getAllProductsPaged(pageable))
        );
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<PagedModel<ProductDTO>> getAllProductsPagedByStoreAndCategory(
            @PathVariable UUID storeId,
            @RequestParam(required = false) String categoryName,
            @PageableDefault(size = 12) Pageable pageable
    ) {
        return ResponseEntity.ok().body(
                new PagedModel<>(productService.getAllProductsPagedByStoreAndCategory(storeId, categoryName, pageable))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findProductById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(productService.findProductById(id));
    }

    @PostMapping
    public ResponseEntity<ProductDTO> insertNewProduct(@RequestBody @Valid NewProductDTO dto) {
        var productDto = productService.insertNewProduct(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/{id}")
                .buildAndExpand(productDto.id())
                .toUri();

        return ResponseEntity.created(uri).body(productDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProductById(@PathVariable UUID id, @RequestBody ProductDTO dto) {
        var productDto = productService.updateProduct(dto);
        return ResponseEntity.ok().body(productDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable UUID id) {
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }
}