package com.uevitondev.deliverybackend.domain.store;

import jakarta.validation.Valid;
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
    public ResponseEntity<List<StoreDTO>> getAllStores() {
        return ResponseEntity.ok().body(storeService.findAllStores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreDTO> getPizzeriaById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(storeService.findStoreById(id));
    }

    @PostMapping
    public ResponseEntity<StoreDTO> insertNewStore(@RequestBody @Valid StoreDTO dto) {
        dto = storeService.insertNewStore(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StoreDTO> updateStoreById(@PathVariable UUID id, @RequestBody @Valid StoreDTO dto) {
        dto = storeService.updateStoreById(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStoreById(@PathVariable UUID id) {
        storeService.deleteStoreById(id);
        return ResponseEntity.noContent().build();
    }

}