package com.uevitondev.deliverybackend.domain.address;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/delivery/v1/api/adresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public ResponseEntity<List<AddressDTO>> getAllAddresses() {
        return ResponseEntity.ok().body(addressService.findAllAddresses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(addressService.findAddressById(id));
    }

    @PostMapping
    public ResponseEntity<AddressDTO> insertNewAddress(@RequestBody @Valid AddressDTO dto) {
        dto = addressService.insertNewAddress(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressDTO> updateCategoryById(@PathVariable UUID id, @RequestBody AddressDTO dto) {
        dto = addressService.updateAddressById(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable UUID id) {
        addressService.deleteAddressById(id);
        return ResponseEntity.noContent().build();
    }
}
