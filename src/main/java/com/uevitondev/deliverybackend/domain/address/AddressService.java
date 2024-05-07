package com.uevitondev.deliverybackend.domain.address;

import com.uevitondev.deliverybackend.domain.exception.DatabaseException;
import com.uevitondev.deliverybackend.domain.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Transactional(readOnly = true)
    public List<AddressDTO> findAllAddresses() {
        return addressRepository.findAll().stream().map(AddressDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public AddressDTO findAddressById(UUID id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("address not found for addressId: " + id));
        return new AddressDTO(address);
    }

    @Transactional
    public AddressDTO insertNewAddress(AddressDTO dto) {
        Address address = new Address();
        address.setZipCode(dto.getZipCode());
        address.setUf(dto.getUf());
        address.setCity(dto.getCity());
        address.setDistrict(dto.getDistrict());
        address.setStreet(dto.getStreet());
        address.setComplement(dto.getComplement());
        address.setNumber(dto.getNumber());
        address = addressRepository.save(address);

        return new AddressDTO(address);
    }

    @Transactional
    public AddressDTO updateAddressById(UUID id, AddressDTO dto) {
        try {
            Address address = addressRepository.getReferenceById(id);
            address.setZipCode(dto.getZipCode());
            address.setUf(dto.getUf());
            address.setCity(dto.getCity());
            address.setDistrict(dto.getDistrict());
            address.setStreet(dto.getStreet());
            address.setComplement(dto.getComplement());
            address.setNumber(dto.getNumber());
            address.setUpdateAt(LocalDateTime.now());
            address = addressRepository.save(address);
            return new AddressDTO(address);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("address not found for addressId: " + id);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteAddressById(UUID id) {
        if (!addressRepository.existsById(id)) {
            throw new ResourceNotFoundException("address not found for addressId: " + id);
        }
        try {
            addressRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Referential integrity constraint violation");
        }
    }
}