package com.uevitondev.deliverybackend.domain.address;

import com.uevitondev.deliverybackend.domain.exception.DatabaseException;
import com.uevitondev.deliverybackend.domain.exception.ResourceNotFoundException;
import com.uevitondev.deliverybackend.domain.store.Store;
import com.uevitondev.deliverybackend.domain.user.User;
import com.uevitondev.deliverybackend.domain.user.UserService;
import com.uevitondev.deliverybackend.domain.utils.ViaCepService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AddressService {

    private final UserAddressRepository userAddressRepository;
    private final StoreAddressRepository storeAddressRepository;
    private final UserService userService;
    private final ViaCepService viaCepService;


    public AddressService(
            UserAddressRepository userAddressRepository,
            StoreAddressRepository storeAddressRepository,
            UserService userService,
            ViaCepService viaCepService
    ) {

        this.userAddressRepository = userAddressRepository;
        this.storeAddressRepository = storeAddressRepository;
        this.userService = userService;
        this.viaCepService = viaCepService;
    }

    public Address findById(UUID id) {
        return userAddressRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("address not found"));
    }

    public AddressViaCepDTO findAddressViaCepByCep(String cep) {
        return viaCepService.findAddressByCep(cep);
    }

    public List<AddressDTO> findAllUserAddresses() {
        return userAddressRepository.findAll().stream().map(AddressDTO::new).toList();
    }

    public List<AddressDTO> findAllAddressesByUser(User user) {
        return userAddressRepository.findAllByUser(user).stream().map(AddressDTO::new).toList();
    }

    public AddressDTO findUserAddressById(UUID id) {
        UserAddress userAddress = userAddressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));
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
                .orElseThrow(() -> new ResourceNotFoundException("store address not found"));
        return new AddressDTO(storeAddress);
    }

    @Transactional
    public AddressDTO insertNewUserAddress(AddressDTO dto) {
        var userAddress = mapperAddressDtoFromNewUserAddress(dto);
        return new AddressDTO(userAddressRepository.save(userAddress));
    }

    @Transactional
    public AddressDTO updateUserAddress(AddressDTO dto) {
        var userAddress = userAddressRepository.findById(dto.id()).orElseThrow(
                () -> new ResourceNotFoundException("user address not found")
        );
        userAddress = mapperAddressDtoFromUpdateUserAddress(userAddress, dto);
        return new AddressDTO(userAddressRepository.save(userAddress));

    }

    @Transactional
    public void deleteAddressById(UUID id) {
        if (!userAddressRepository.existsById(id)) {
            throw new ResourceNotFoundException("resource not found");
        }
        try {
            userAddressRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("integrity constraint violation");
        }
    }

    public UserAddress mapperAddressDtoFromNewUserAddress(AddressDTO dto) {

        var userAddress = new UserAddress(
                null,
                dto.name(),
                dto.phoneNumber(),
                dto.street(),
                dto.number(),
                dto.district(),
                dto.city(),
                dto.uf(),
                dto.complement(),
                dto.zipCode()
        );
        userAddress.setUser(userService.getUserAuthenticated());
        return userAddress;

    }

    public UserAddress mapperAddressDtoFromUpdateUserAddress(UserAddress userAddress, AddressDTO dto) {
        userAddress.setName(dto.name());
        userAddress.setPhoneNumber(dto.phoneNumber());
        userAddress.setStreet(dto.street());
        userAddress.setNumber(dto.number());
        userAddress.setDistrict(dto.district());
        userAddress.setCity(dto.city());
        userAddress.setUf(dto.uf());
        userAddress.setComplement(dto.complement());
        userAddress.setZipCode(dto.zipCode());
        userAddress.setUpdatedAt(LocalDateTime.now());
        return userAddress;
    }
}