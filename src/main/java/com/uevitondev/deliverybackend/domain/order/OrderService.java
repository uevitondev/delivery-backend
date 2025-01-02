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
import com.uevitondev.deliverybackend.domain.payment.PaymentService;
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
public class OrderService {

    private final OrderRepository orderRepository;
    private final PaymentService paymentService;
    private final UserService userService;
    private final StoreService storeService;
    private final AddressService addressService;
    private final ProductRepository productRepository;


    public OrderService(
            OrderRepository orderRepository,
            PaymentService paymentService,
            UserService userService,
            StoreService storeService,
            AddressService addressService,
            ProductRepository productRepository
    ) {

        this.orderRepository = orderRepository;
        this.paymentService = paymentService;
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
        var order = orderRepository.findByIdWithStoreAndOrderItemsDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("order not found"));

        var store = order.getStore();
        var deliveryAddress = order.getDeliveryAddress();

        return new OrderDetailsDTO(
                order.getId(),
                order.getNumber(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                order.getClosedAt(),
                order.getStatus().toString(),
                order.getPaymentMethod().toString(),
                order.getTotal(),
                new StoreDTO(
                        store.getId(),
                        store.getLogoUrl(),
                        store.getName(),
                        store.getPhoneNumber(),
                        store.getType()
                ),
                new AddressDTO(
                        deliveryAddress.getId(),
                        deliveryAddress.getName(),
                        deliveryAddress.getPhoneNumber(),
                        deliveryAddress.getZipCode(),
                        deliveryAddress.getStreet(),
                        deliveryAddress.getNumber(),
                        deliveryAddress.getDistrict(),
                        deliveryAddress.getCity(),
                        deliveryAddress.getUf(),
                        deliveryAddress.getComplement()
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
            var paymentMethod = paymentService.findPaymentMethodById(dto.paymentMethodId());
            var deliveryAddress = new DeliveryAddress(
                    null,
                    address.getName(),
                    address.getPhoneNumber(),
                    address.getStreet(),
                    address.getNumber(),
                    address.getDistrict(),
                    address.getCity(),
                    address.getUf(),
                    address.getComplement(),
                    address.getZipCode(),
                    address.getCreatedAt(),
                    address.getUpdatedAt()
            );

            var order = new Order(
                    null,
                    OrderStatus.PENDENTE,
                    paymentMethod.getName(),
                    customer,
                    store
            );
            order.addDeliveryAddress(deliveryAddress);
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
