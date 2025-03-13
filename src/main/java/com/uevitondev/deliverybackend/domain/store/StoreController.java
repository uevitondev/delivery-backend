package com.uevitondev.deliverybackend.domain.store;

import com.uevitondev.deliverybackend.domain.product.ProductDTO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/stores")
public class StoreController {
    private static final Logger LOGGER = LoggerFactory.getLogger(StoreController.class);

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping
    public ResponseEntity<List<StoreDTO>> getAllStores(@RequestParam(value = "name", required = false) String name) {
        var storesDto = storeService.findAllStoresWithFilters(name).stream().map(StoreDTO::new).toList();
        return ResponseEntity.ok().body(storesDto);
    }


    @GetMapping("/name")
    public ResponseEntity<StoreDTO> getStoreByName(@RequestParam(value = "name", required = true) String name) {
        var storeDto = new StoreDTO(storeService.findStoreByName(name));
        return ResponseEntity.ok().body(storeDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreDTO> getStoreById(@PathVariable UUID id) {
        var storeDto = new StoreDTO(storeService.findById(id));
        return ResponseEntity.ok().body(storeDto);
    }

    @GetMapping("/seller/list")
    public ResponseEntity<List<StoreDTO>> getStoresBySeller() {
        var storeDtos = storeService.findStoresBySeller().stream().map(StoreDTO::new).toList();
        return ResponseEntity.ok().body(storeDtos);
    }

    @PostMapping
    public ResponseEntity<StoreDTO> insertNewStore(
            @RequestPart("logoFile") @Valid MultipartFile logoFile,
            @RequestPart("newStore") @Valid NewStoreDTO dto
    ) {
        var storeDto = new StoreDTO(storeService.insertNewStore(logoFile, dto));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(storeDto.id()).toUri();
        return ResponseEntity.created(uri).body(storeDto);
    }

    @PutMapping("/update")
    public ResponseEntity<StoreDTO> updateStoreById(@Valid @RequestBody StoreDTO dto) {
        dto = new StoreDTO(storeService.updateStore(dto));
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStoreById(@PathVariable UUID id) {
        storeService.deleteStoreById(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{store_id}/products")
    public ResponseEntity<PagedModel<ProductDTO>> getAllProductsByStoreId(
            @PathVariable(name = "store_id") UUID storeId,
            @RequestParam(name = "product_name", required = false) String productName,
            @RequestParam(name = "category_name", required = false) String categoryName,
            @PageableDefault( page = 0, size = 12, direction = Sort.Direction.ASC) Pageable pageable
    ) {
        var products = storeService.findAllProductsByStoreId(storeId, productName, categoryName, pageable)
                .map(ProductDTO::new);
        return ResponseEntity.ok().body(new PagedModel<>(products));
    }


}