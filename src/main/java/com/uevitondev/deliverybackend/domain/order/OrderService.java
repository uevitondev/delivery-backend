package com.uevitondev.deliverybackend.domain.order;

import com.uevitondev.deliverybackend.domain.address.Address;
import com.uevitondev.deliverybackend.domain.address.AddressRepository;
import com.uevitondev.deliverybackend.domain.customer.Customer;
import com.uevitondev.deliverybackend.domain.enums.OrderStatus;
import com.uevitondev.deliverybackend.domain.exception.DatabaseException;
import com.uevitondev.deliverybackend.domain.exception.ResourceNotFoundException;
import com.uevitondev.deliverybackend.domain.orderitem.OrderItem;
import com.uevitondev.deliverybackend.domain.orderitem.OrderItemRepository;
import com.uevitondev.deliverybackend.domain.product.Product;
import com.uevitondev.deliverybackend.domain.product.ProductRepository;
import com.uevitondev.deliverybackend.domain.store.Store;
import com.uevitondev.deliverybackend.domain.store.StoreRepository;
import com.uevitondev.deliverybackend.domain.user.User;
import com.uevitondev.deliverybackend.domain.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                        StoreRepository storeRepository, AddressRepository addressRepository,
                        ProductRepository productRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.storeRepository = storeRepository;
        this.addressRepository = addressRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
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
            var username = SecurityContextHolder.getContext().getAuthentication().getName();
            var customer = (Customer) userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("not found"));
            Store store = storeRepository.findById(dto.getStoreId())
                    .orElseThrow(() -> new ResourceNotFoundException("store not found for storeId: " + dto.getStoreId()));
            Address address = addressRepository.findById(dto.getAddressId())
                    .orElseThrow(() -> new ResourceNotFoundException("address not found for addressId: " + dto.getAddressId()));

            Order order = new Order(OrderStatus.PENDENTE, customer, saveOrderItem(dto.getCartItems()));
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
