package com.uevitondev.deliverybackend.domain.address;

import com.uevitondev.deliverybackend.domain.exception.DatabaseException;
import com.uevitondev.deliverybackend.domain.exception.ResourceNotFoundException;
import com.uevitondev.deliverybackend.domain.store.Store;
import com.uevitondev.deliverybackend.domain.user.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AddressService {

    private final UserAddressRepository userAddressRepository;
    private final StoreAddressRepository storeAddressRepository;


    public AddressService(UserAddressRepository userAddressRepository, StoreAddressRepository storeAddressRepository) {
        this.userAddressRepository = userAddressRepository;
        this.storeAddressRepository = storeAddressRepository;
    }

    public List<AddressDTO> findAllUserAddresses() {
        return userAddressRepository.findAll().stream().map(AddressDTO::new).toList();
    }

    public List<AddressDTO> findAllAddressesByUser(User user) {
        return userAddressRepository.findAllByUser(user).stream().map(AddressDTO::new).toList();
    }

    public AddressDTO findUserAddressById(UUID id) {
        UserAddress userAddress = userAddressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user address not found for id: " + id));
        return new AddressDTO(userAddress);
    }

    public List<AddressDTO> findAllStoreAddresses() {
        return storeAddressRepository.findAll().stream().map(AddressDTO::new).toList();
    }

    public List<AddressDTO> findAllAddressesByStore(Store store) {
        return storeAddressRepository.findAllByStore(store).stream().map(AddressDTO::new).toList();
    }

    public AddressDTO findStoreAddressById(UUID id) {
        UserAddress userAddress = userAddressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("store address not found for id: " + id));
        return new AddressDTO(userAddress);
    }


    public AddressDTO insertNewAddress(AddressDTO dto) {
        UserAddress userAddress = new UserAddress();
        userAddress.setZipCode(dto.getZipCode());
        userAddress.setUf(dto.getUf());
        userAddress.setCity(dto.getCity());
        userAddress.setDistrict(dto.getDistrict());
        userAddress.setStreet(dto.getStreet());
        userAddress.setComplement(dto.getComplement());
        userAddress.setNumber(dto.getNumber());
        userAddress = userAddressRepository.save(userAddress);

        return new AddressDTO(userAddress);
    }

    public AddressDTO updateAddressById(UUID id, AddressDTO dto) {
        try {
            UserAddress userAddress = userAddressRepository.getReferenceById(id);
            userAddress.setZipCode(dto.getZipCode());
            userAddress.setUf(dto.getUf());
            userAddress.setCity(dto.getCity());
            userAddress.setDistrict(dto.getDistrict());
            userAddress.setStreet(dto.getStreet());
            userAddress.setComplement(dto.getComplement());
            userAddress.setNumber(dto.getNumber());
            userAddress.setUpdatedAt(LocalDateTime.now());
            userAddress = userAddressRepository.save(userAddress);
            return new AddressDTO(userAddress);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("userAddress not found for userAddressId: " + id);
        }
    }

    public void deleteAddressById(UUID id) {
        if (!userAddressRepository.existsById(id)) {
            throw new ResourceNotFoundException("userAddress not found for userAddressId: " + id);
        }
        try {
            userAddressRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Referential integrity constraint violation");
        }
    }
}