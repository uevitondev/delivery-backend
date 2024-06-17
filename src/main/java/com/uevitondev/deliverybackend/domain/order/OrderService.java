package com.uevitondev.deliverybackend.domain.order;

import com.uevitondev.deliverybackend.domain.address.UserAddressRepository;
import com.uevitondev.deliverybackend.domain.customer.Customer;
import com.uevitondev.deliverybackend.domain.exception.DatabaseException;
import com.uevitondev.deliverybackend.domain.exception.ResourceNotFoundException;
import com.uevitondev.deliverybackend.domain.orderitem.CartItemDTO;
import com.uevitondev.deliverybackend.domain.orderitem.OrderItem;
import com.uevitondev.deliverybackend.domain.orderitem.OrderItemRepository;
import com.uevitondev.deliverybackend.domain.orderitem.ShoppingCartDTO;
import com.uevitondev.deliverybackend.domain.product.Product;
import com.uevitondev.deliverybackend.domain.product.ProductRepository;
import com.uevitondev.deliverybackend.domain.store.StoreRepository;
import com.uevitondev.deliverybackend.domain.user.UserRepository;
import com.uevitondev.deliverybackend.domain.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
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
            Order order = new Order(
                    OrderStatus.PENDENTE,
                    OrderPayment.PIX,
                    (Customer) UserService.getUserAuthenticated(),
                    storeRepository.findById(dto.getStoreId())
                            .orElseThrow(() -> new ResourceNotFoundException("store not found for storeId: " + dto.getStoreId())),
                    userAddressRepository.findById(dto.getUserAddressId())
                            .orElseThrow(() -> new ResourceNotFoundException("address not found for addressId: " + dto.getUserAddressId()))
            );
            addOrderItemsToOrder(order, dto.getCartItems());
            return new OrderDTO(orderRepository.save(order));

        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Referential integrity constraint violation");
        }
    }

    public void addOrderItemsToOrder(Order order, Set<CartItemDTO> cartItems) {
        for (CartItemDTO cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("product not found for productId: " + cartItem.getProductId()));
            OrderItem orderItem = new OrderItem(cartItem.getQuantity(), cartItem.getObservation(), product);
            order.addOrderItem(orderItem);
        }
    }


}
