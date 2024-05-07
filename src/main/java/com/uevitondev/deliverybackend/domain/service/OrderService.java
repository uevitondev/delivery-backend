package com.uevitondev.deliverybackend.domain.service;

import com.uevitondev.deliverybackend.domain.dto.OrderDTO;
import com.uevitondev.deliverybackend.domain.enums.OrderStatus;
import com.uevitondev.deliverybackend.domain.exception.DatabaseException;
import com.uevitondev.deliverybackend.domain.exception.ResourceNotFoundException;
import com.uevitondev.deliverybackend.domain.model.Address;
import com.uevitondev.deliverybackend.domain.model.Order;
import com.uevitondev.deliverybackend.domain.model.Product;
import com.uevitondev.deliverybackend.domain.model.Store;
import com.uevitondev.deliverybackend.domain.repository.AddressRepository;
import com.uevitondev.deliverybackend.domain.repository.OrderRepository;
import com.uevitondev.deliverybackend.domain.repository.ProductRepository;
import com.uevitondev.deliverybackend.domain.repository.StoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final StoreRepository storeRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                        StoreRepository storeRepository, AddressRepository addressRepository,
                        ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.storeRepository = storeRepository;
        this.addressRepository = addressRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<OrderDTO> findAllOrders() {
        return orderRepository.findAll().stream().map(OrderDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public OrderDTO findOrderById(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("order not found for orderId: " + id));
        return new OrderDTO(order);
    }


    @Transactional
    public OrderDTO saveNewOrder(ShoppingCartDTO dto) {
        try {
            var user = new Customer();
            //var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            // var user = (PhysicalUser) authService.getUserAuthenticated();
            Store store = storeRepository.findById(dto.getPizzeriaId())
                    .orElseThrow(() -> new ResourceNotFoundException("store not found for storeId: " + dto.getPizzeriaId()));
            Address address = addressRepository.findById(dto.getAddressId())
                    .orElseThrow(() -> new ResourceNotFoundException("address not found for addressId: " + dto.getAddressId()));

            Order order = new Order(OrderStatus.PENDENTE, user, saveOrderItem(dto.getCartItems()));
            order.setStore(store);
            order.setAddress(address);

            return new OrderDTO(orderRepository.save(order));

        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Referential integrity constraint violation");
        }
    }

    public Set<OrderItem> saveOrderItem(Set<CartItemDTO> cartItems) {
        Set<OrderItem> orderItems = new HashSet<>();
        for (CartItemDTO cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("product not found for productId: " + cartItem.getProductId()));
            OrderItem orderItem = new OrderItem(cartItem.getQuantity(), cartItem.getObservation(), product);
            orderItem = orderItemRepository.save(orderItem);
            orderItems.add(orderItem);
        }
        return orderItems;
    }


}
