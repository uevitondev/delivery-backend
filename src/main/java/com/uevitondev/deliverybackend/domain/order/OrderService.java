package com.uevitondev.deliverybackend.domain.order;

import com.uevitondev.deliverybackend.domain.address.UserAddress;
import com.uevitondev.deliverybackend.domain.address.UserAddressRepository;
import com.uevitondev.deliverybackend.domain.customer.Customer;
import com.uevitondev.deliverybackend.domain.enums.OrderStatus;
import com.uevitondev.deliverybackend.domain.exception.DatabaseException;
import com.uevitondev.deliverybackend.domain.exception.ResourceNotFoundException;
import com.uevitondev.deliverybackend.domain.orderitem.CartItemDTO;
import com.uevitondev.deliverybackend.domain.orderitem.OrderItem;
import com.uevitondev.deliverybackend.domain.orderitem.OrderItemRepository;
import com.uevitondev.deliverybackend.domain.orderitem.ShoppingCartDTO;
import com.uevitondev.deliverybackend.domain.product.Product;
import com.uevitondev.deliverybackend.domain.product.ProductRepository;
import com.uevitondev.deliverybackend.domain.store.Store;
import com.uevitondev.deliverybackend.domain.store.StoreRepository;
import com.uevitondev.deliverybackend.domain.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final StoreRepository storeRepository;
    private final UserAddressRepository userAddressRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                        StoreRepository storeRepository, UserAddressRepository userAddressRepository,
                        ProductRepository productRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.storeRepository = storeRepository;
        this.userAddressRepository = userAddressRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }


    public List<OrderDTO> findAllOrders() {
        return orderRepository.findAll().stream().map(OrderDTO::new).toList();
    }

    public List<OrderDTO> findAllOrdersByCustomer(Customer customer) {
        return orderRepository.findOrdersByCustomer(customer).stream().map(OrderDTO::new).toList();
    }


    public OrderDTO findOrderById(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("order not found for orderId: " + id));
        return new OrderDTO(order);
    }


    public OrderDTO saveNewOrder(ShoppingCartDTO dto) {
        try {
            var username = SecurityContextHolder.getContext().getAuthentication().getName();
            var customer = (Customer) userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("not found"));
            Store store = storeRepository.findById(dto.getStoreId())
                    .orElseThrow(() -> new ResourceNotFoundException("store not found for storeId: " + dto.getStoreId()));
            UserAddress address = userAddressRepository.findById(dto.getAddressId())
                    .orElseThrow(() -> new ResourceNotFoundException("address not found for addressId: " + dto.getAddressId()));

            var orderItems = getOrderItems(dto.getCartItems());
            Order order = new Order(OrderStatus.PENDENTE, store, customer, address, orderItems);
            orderItemRepository.saveAll(orderItems);
            return new OrderDTO(orderRepository.save(order));

        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Referential integrity constraint violation");
        }
    }

    public List<OrderItem> getOrderItems(Set<CartItemDTO> cartItems) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItemDTO cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("product not found for productId: " + cartItem.getProductId()));
            OrderItem orderItem = new OrderItem(cartItem.getQuantity(), cartItem.getObservation(), product);
            orderItems.add(orderItem);
        }
        return orderItems;
    }


}
