package com.uevitondev.deliverybackend.domain.address;

import com.uevitondev.deliverybackend.domain.exception.DatabaseException;
import com.uevitondev.deliverybackend.domain.exception.ResourceNotFoundException;
import com.uevitondev.deliverybackend.domain.store.Store;
import com.uevitondev.deliverybackend.domain.user.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
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
        StoreAddress storeAddress = storeAddressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("store address not found for id: " + id));
        return new AddressDTO(storeAddress);
    }


    public AddressDTO insertNewAddress(AddressDTO dto) {
        UserAddress userAddress = (UserAddress) mapperAddressDtoFromNewAddress(dto);
        return new AddressDTO(userAddressRepository.save(userAddress));
    }

    public AddressDTO updateAddressById(UUID addressId, AddressDTO dto) {
        try {

            UserAddress userAddress = (UserAddress) mapperAddressDtoFromUpdateAddress(
                    dto,
                    userAddressRepository.getReferenceById(addressId)
            );


            userAddress = userAddressRepository.save(userAddress);
            return new AddressDTO(userAddress);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("userAddress not found for userAddressId: " + addressId);
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

    public Address mapperAddressDtoFromNewAddress(AddressDTO dto) {
        return new Address(
                null,
                dto.name(),
                dto.phoneNumber(),
                dto.street(),
                dto.number(),
                dto.district(),
                dto.city(),
                dto.uf(),
                dto.complement(),
                dto.zipCode(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public Address mapperAddressDtoFromUpdateAddress(AddressDTO dto, Address address) {
        address.setName(dto.name());
        address.setPhoneNumber(dto.phoneNumber());
        address.setStreet(dto.street());
        address.setNumber(dto.number());
        address.setDistrict(dto.district());
        address.setCity(dto.city());
        address.setUf(dto.uf());
        address.setComplement(dto.complement());
        address.setZipCode(dto.zipCode());
        address.setUpdatedAt(LocalDateTime.now());

        return address;
    }
}