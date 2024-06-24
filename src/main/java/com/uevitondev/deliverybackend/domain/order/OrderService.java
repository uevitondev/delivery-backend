package com.uevitondev.deliverybackend.domain.order;

import com.uevitondev.deliverybackend.domain.address.AddressDTO;
import com.uevitondev.deliverybackend.domain.address.UserAddressRepository;
import com.uevitondev.deliverybackend.domain.customer.Customer;
import com.uevitondev.deliverybackend.domain.exception.DatabaseException;
import com.uevitondev.deliverybackend.domain.exception.ResourceNotFoundException;
import com.uevitondev.deliverybackend.domain.orderitem.CartItemDTO;
import com.uevitondev.deliverybackend.domain.orderitem.OrderItem;
import com.uevitondev.deliverybackend.domain.orderitem.OrderItemDTO;
import com.uevitondev.deliverybackend.domain.orderitem.ShoppingCartDTO;
import com.uevitondev.deliverybackend.domain.product.Product;
import com.uevitondev.deliverybackend.domain.product.ProductRepository;
import com.uevitondev.deliverybackend.domain.store.Store;
import com.uevitondev.deliverybackend.domain.store.StoreRepository;
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
    private final StoreRepository storeRepository;
    private final UserAddressRepository userAddressRepository;
    private final ProductRepository productRepository;


    public OrderService(OrderRepository orderRepository, StoreRepository storeRepository,
                        UserAddressRepository userAddressRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.storeRepository = storeRepository;
        this.userAddressRepository = userAddressRepository;
        this.productRepository = productRepository;
    }


    public List<OrderDTO> findAllOrders() {
        return orderRepository.findAll().stream().map(OrderDTO::new).toList();
    }

    public List<OrderDTO> findAllOrdersByCustomer(Customer customer) {
        return orderRepository.findByCustomer(customer).stream().map(OrderDTO::new).toList();
    }


    public OrderDTO findOrderById(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("order not found for orderId: " + id));
        return new OrderDTO(order);
    }

    public OrderCustomerDTO findOrderByIdWithOrderItems(UUID id) {
        Order order = orderRepository.findByIdWithOrderItems(id)
                .orElseThrow(() -> new ResourceNotFoundException("order not found for orderId: " + id));

        return new OrderCustomerDTO(
                order.getId(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                order.getClosedAt(),
                order.getStatus(),
                order.getPaymentMethod(),
                order.getTotal(),
                getOrderCustomerDataForCustomerOrder(order.getCustomer()),
                getOrderStoreDataForStoreOrder(order.getStore()),
                new AddressDTO(order.getAddress()),
                order.getOrderItems().stream().map(OrderItemDTO::new).toList()
        );
    }

    public OrderCustomerDataDTO getOrderCustomerDataForCustomerOrder(Customer customer) {
        return new OrderCustomerDataDTO(
                customer.getFirstName(),
                customer.getLastName(),
                customer.getPhoneNumber()
        );
    }

    public OrderStoreDataDTO getOrderStoreDataForStoreOrder(Store store) {
        return new OrderStoreDataDTO(
                store.getId(),
                store.getLogoUrl(),
                store.getName(),
                store.getPhoneNumber(),
                store.getType()
        );
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
