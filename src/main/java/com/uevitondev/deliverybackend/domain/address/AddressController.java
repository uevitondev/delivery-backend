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

    private final UserService userService;
    private final AddressService addressService;

    public AddressController(UserService userService, AddressService addressService) {
        this.userService = userService;
        this.addressService = addressService;
    }

    @GetMapping("/viacep")
    public ResponseEntity<AddressViaCepDTO>  getAddressViaCepByCep(@RequestParam String cep){
        return ResponseEntity.ok().body(addressService.findAddressViaCepByCep(cep));
    }


    @GetMapping("/users")
    public ResponseEntity<List<AddressDTO>> getAllAddressesForAllUsers() {
        return ResponseEntity.ok().body(addressService.findAllUserAddresses());
    }

    @GetMapping("/user/all")
    public ResponseEntity<List<AddressDTO>> getAllAddressesByUser() {
        return ResponseEntity.ok().body(addressService.findAllAddressesByUser(userService.getUserAuthenticated()));
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
    public ResponseEntity<AddressDTO> insertNewAddress(@Valid @RequestBody AddressDTO dto) {
        dto = addressService.insertNewUserAddress(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.id()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping("/update")
    public ResponseEntity<AddressDTO> updateAddressById(@Valid @RequestBody AddressDTO dto) {
        dto = addressService.updateUserAddress(dto);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddressById(@PathVariable UUID id) {
        addressService.deleteAddressById(id);
        return ResponseEntity.noContent().build();
    }
}
