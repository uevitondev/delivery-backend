package com.uevitondev.deliverybackend.domain.store;

import com.uevitondev.deliverybackend.domain.product.ProductDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/stores")
public class StoreController {

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

    @PostMapping
    public ResponseEntity<StoreDTO> insertNewStore(@RequestBody @Valid StoreDTO dto) {
        dto = new StoreDTO(storeService.insertNewStore(dto));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.id()).toUri();
        return ResponseEntity.created(uri).body(dto);
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


    @GetMapping("/{id}/products")
    public ResponseEntity<PagedModel<ProductDTO>> getAllProductsByStoreId(
            @PathVariable UUID id,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "category", required = false) String category,
            @PageableDefault(size = 12) Pageable pageable
    ) {
        var products = storeService.findAllProductsByStoreId(id, name, category, pageable).map(ProductDTO::new);
        return ResponseEntity.ok().body(new PagedModel<>(products));
    }


}