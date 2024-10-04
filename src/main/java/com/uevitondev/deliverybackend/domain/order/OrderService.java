package com.uevitondev.deliverybackend.domain.order;

import com.uevitondev.deliverybackend.domain.address.AddressDTO;
import com.uevitondev.deliverybackend.domain.address.AddressService;
import com.uevitondev.deliverybackend.domain.customer.Customer;
import com.uevitondev.deliverybackend.domain.exception.DatabaseException;
import com.uevitondev.deliverybackend.domain.exception.ResourceNotFoundException;
import com.uevitondev.deliverybackend.domain.orderitem.CartItemDTO;
import com.uevitondev.deliverybackend.domain.orderitem.OrderItem;
import com.uevitondev.deliverybackend.domain.orderitem.OrderItemDTO;
import com.uevitondev.deliverybackend.domain.orderitem.ShoppingCartDTO;
import com.uevitondev.deliverybackend.domain.product.ProductRepository;
import com.uevitondev.deliverybackend.domain.store.StoreDTO;
import com.uevitondev.deliverybackend.domain.store.StoreService;
import com.uevitondev.deliverybackend.domain.user.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final StoreService storeService;
    private final AddressService addressService;
    private final ProductRepository productRepository;


    public OrderService(
            OrderRepository orderRepository,
            UserService userService,
            StoreService storeService,
            AddressService addressService,
            ProductRepository productRepository
    ) {

        this.orderRepository = orderRepository;
        this.userService = userService;
        this.storeService = storeService;
        this.addressService = addressService;
        this.productRepository = productRepository;
    }


    public List<OrderDTO> findAllOrders() {
        return orderRepository.findAll().stream().map(OrderDTO::new).toList();
    }

    public List<OrderDTO> findAllOrdersByCustomer(Customer customer) {
        return orderRepository.findByCustomer(customer).stream().map(OrderDTO::new).toList();
    }

    public OrderDTO findOrderById(UUID id) {
        var order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("order not found"));
        return new OrderDTO(order);
    }

    public OrderDetailsDTO findOrderByIdWithOrderItems(UUID id) {
        var order = orderRepository.findByIdWithOrderItems(id)
                .orElseThrow(() -> new ResourceNotFoundException("order not found"));

        var store = order.getStore();
        var orderDelivery = order.getOrderDelivery();

        return new OrderDetailsDTO(
                order.getId(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                order.getClosedAt(),
                order.getStatus(),
                order.getPaymentMethod(),
                order.getTotal(),
                 new OrderCustomerDTO(
                         orderDelivery.getDeliveryName(),
                         orderDelivery.getDeliveryPhoneNumber()
                 )
                ,
                new StoreDTO(
                        store.getId(),
                        store.getLogoUrl(),
                        store.getName(),
                        store.getPhoneNumber(),
                        store.getType()
                ),
                new AddressDTO(
                        orderDelivery.getId(),
                        orderDelivery.getDeliveryName(),
                        orderDelivery.getDeliveryPhoneNumber(),
                        orderDelivery.getDeliveryStreet(),
                        orderDelivery.getDeliveryNumber(),
                        orderDelivery.getDeliveryDistrict(),
                        orderDelivery.getDeliveryCity(),
                        orderDelivery.getDeliveryUf(),
                        orderDelivery.getDeliveryComplement(),
                        orderDelivery.getDeliveryZipCode()
                ),
                order.getOrderItems().stream().map(OrderItemDTO::new).toList()
        );
    }


    @Transactional
    public OrderDTO saveNewOrder(ShoppingCartDTO dto) {
        try {
            var customer = (Customer) userService.getUserAuthenticated();
            var store = storeService.findById(dto.storeId());
            var address = addressService.findById(dto.addressId());

            var orderDelivery = new OrderDelivery(
                    null,
                    address.getName(),
                    address.getPhoneNumber(),
                    address.getStreet(),
                    address.getNumber(),
                    address.getDistrict(),
                    address.getCity(),
                    address.getUf(),
                    address.getComplement(),
                    address.getZipCode()
            );

            var order = new Order(
                    OrderStatus.PENDENTE,
                    OrderPayment.PIX,
                    customer,
                    store
            );
            order.addOrderDelivery(orderDelivery);
            addOrderItemsToOrder(order, dto.cartItems());
            return new OrderDTO(orderRepository.save(order));

        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("integrity constraint violation");
        }
    }

    public void addOrderItemsToOrder(Order order, Set<CartItemDTO> cartItemsDtos) {
        for (CartItemDTO cartItem : cartItemsDtos) {
            order.addOrderItem(getOrderItemFromCartItem(cartItem));
        }
    }

    public OrderItem getOrderItemFromCartItem(CartItemDTO cartItemDTO) {
        var productId = cartItemDTO.product().id();
        var product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("product not found")
        );
        return new OrderItem(product, cartItemDTO.quantity(), cartItemDTO.note());
    }

}
