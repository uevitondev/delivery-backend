package com.uevitondev.deliverybackend.domain.address;

import com.uevitondev.deliverybackend.domain.store.Store;
import com.uevitondev.deliverybackend.domain.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }


    @GetMapping("/users")
    public ResponseEntity<List<AddressDTO>> getAllAddressesForAllUsers() {
        return ResponseEntity.ok().body(addressService.findAllUserAddresses());
    }

    @GetMapping("/user/all")
    public ResponseEntity<List<AddressDTO>> getAllAddressesByUser() {
        return ResponseEntity.ok().body(addressService.findAllAddressesByUser(UserService.getUserAuthenticated()));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<AddressDTO> getAddressForUserById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(addressService.findUserAddressById(id));
    }

    @GetMapping("/stores")
    public ResponseEntity<List<AddressDTO>> getAllStoreAddresses() {
        return ResponseEntity.ok().body(addressService.findAllUserAddresses());
    }

    @GetMapping("/store/all")
    public ResponseEntity<List<AddressDTO>> getAllAddressesByStore() {
        return ResponseEntity.ok().body(addressService.findAllAddressesByStore(new Store()));
    }

    @GetMapping("/store/{id}")
    public ResponseEntity<AddressDTO> getStoreAddressById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(addressService.findStoreAddressById(id));
    }

    @PostMapping("/new")
    public ResponseEntity<AddressDTO> insertNewAddress(@RequestBody @Valid AddressDTO dto) {
        dto = addressService.insertNewAddress(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.id()).toUri();
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
