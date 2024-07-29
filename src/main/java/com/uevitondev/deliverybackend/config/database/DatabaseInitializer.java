package com.uevitondev.deliverybackend.config.database;


import com.uevitondev.deliverybackend.domain.address.StoreAddress;
import com.uevitondev.deliverybackend.domain.address.StoreAddressRepository;
import com.uevitondev.deliverybackend.domain.address.UserAddress;
import com.uevitondev.deliverybackend.domain.address.UserAddressRepository;
import com.uevitondev.deliverybackend.domain.category.Category;
import com.uevitondev.deliverybackend.domain.category.CategoryRepository;
import com.uevitondev.deliverybackend.domain.customer.Customer;
import com.uevitondev.deliverybackend.domain.order.Order;
import com.uevitondev.deliverybackend.domain.order.OrderPayment;
import com.uevitondev.deliverybackend.domain.order.OrderRepository;
import com.uevitondev.deliverybackend.domain.order.OrderStatus;
import com.uevitondev.deliverybackend.domain.orderitem.OrderItem;
import com.uevitondev.deliverybackend.domain.product.Product;
import com.uevitondev.deliverybackend.domain.product.ProductRepository;
import com.uevitondev.deliverybackend.domain.role.Role;
import com.uevitondev.deliverybackend.domain.role.RoleRepository;
import com.uevitondev.deliverybackend.domain.seller.Seller;
import com.uevitondev.deliverybackend.domain.store.Store;
import com.uevitondev.deliverybackend.domain.store.StoreRepository;
import com.uevitondev.deliverybackend.domain.store.StoreType;
import com.uevitondev.deliverybackend.domain.user.User;
import com.uevitondev.deliverybackend.domain.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class DatabaseInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final OrderRepository orderRepository;


    public DatabaseInitializer(
            RoleRepository roleRepository,
            UserRepository userRepository,
            UserAddressRepository userAddressRepository,
            CategoryRepository categoryRepository,
            ProductRepository productRepository,
            StoreRepository storeRepository,
            OrderRepository orderRepository
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.userAddressRepository = userAddressRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {

        // Role
        Role adminRole = new Role("ROLE_ADMIN");
        adminRole = roleRepository.save(adminRole);

        Role customerRole = new Role("ROLE_CUSTOMER");
        customerRole = roleRepository.save(customerRole);

        Role sellerRole = new Role("ROLE_SELLER");
        sellerRole = roleRepository.save(sellerRole);

        User adminUser = new User(
                "Admin",
                "UserAdmin",
                "1199454599985",
                "admin@gmail.com",
                "$2a$10$Y7fk59/1Pg.ig0Goy0yTS.5RgKD18N5J3MYCo5bPYzVpslJqfr4uu"
        );
        adminUser.getRoles().add(adminRole);
        userRepository.save(adminUser);

        Customer customerUser = new Customer(
                "Customer",
                "UserCustomer",
                "1199454599985",
                "customer@gmail.com",
                "$2a$10$Y7fk59/1Pg.ig0Goy0yTS.5RgKD18N5J3MYCo5bPYzVpslJqfr4uu"
        );
        customerUser.getRoles().add(customerRole);
        customerUser = userRepository.save(customerUser);

        // user customer address
        UserAddress addressCustomerUser = new UserAddress(
                "Rua da flores",
                126,
                "Distrito 1",
                "São Paulo",
                "SP",
                "Condominio Residencial ABA - Apt 26 A",
                "03584000",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        addressCustomerUser.setUser(customerUser);
        addressCustomerUser = userAddressRepository.save(addressCustomerUser);

        customerUser.getAddresses().add(addressCustomerUser);
        userRepository.save(customerUser);

        Seller sellerUser = new Seller(
                "Seller",
                "UserSeller",
                "1199454599985",
                "seller@gmail.com",
                "$2a$10$Y7fk59/1Pg.ig0Goy0yTS.5RgKD18N5J3MYCo5bPYzVpslJqfr4uu"
        );
        sellerUser.getRoles().add(sellerRole);
        sellerUser = userRepository.save(sellerUser);


        // category
        Category category1 = new Category("PIZZAS");
        Category category2 = new Category("BEBIDAS");
        Category category3 = new Category("SALGADOS");
        Category category4 = new Category("DOCES");
        categoryRepository.saveAll(List.of(category1, category2, category3, category4));

        // address store
        StoreAddress addressStore1 = new StoreAddress(
                "Rua da Conservação",
                25,
                "Montes Claros",
                "Minas Gerais",
                "MG",
                "Proximo ao posto de combustível",
                "03484000",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // store

        var storeLogoUrl = "https://img.freepik.com/fotos-gratis/frente-do-icone-da-" +
                "cesta-de-compras_187299-40115.jpg?t=st=1719251168~exp=17" +
                "19254768~hmac=5ae1185eacbb75d1489105e68614a3caf9777b6f198e48d6db8deb791f933212&w=740";

        Store store1 = new Store(
                storeLogoUrl,
                "Pizzaria Sabor",
                "119958796542",
                StoreType.PIZZERIA.name(),
                sellerUser,
                addressStore1
        );
        store1.setId(UUID.fromString("005d7c57-04ad-4251-bf05-fdc8b38182aa"));
        store1.addAddress(addressStore1);
        store1 = storeRepository.save(store1);


        // product
        String productImageUrl = "https://images.pexels.com/photos/6941025/pexels-photo-694" +
                "1025.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1";
        String productDescription = "Desperte seus sentidos com a irresistível Pizza Suprema, uma obra-prima " +
                "gastronômica que eleva o prazer de saborear uma boa pizza a um novo patamar.";

        Product product1 = new Product(productImageUrl, "Greek Pizza", productDescription, 10.0);
        product1.setCategory(category1);
        product1.setStore(store1);

        Product product2 = new Product(productImageUrl, "Margherita Pizza", productDescription, 20.0);
        product2.setCategory(category1);
        product2.setStore(store1);

        Product product3 = new Product(productImageUrl, "Coca-Cola 2L", productDescription, 30.0);
        product3.setCategory(category2);
        product3.setStore(store1);

        Product product4 = new Product(productImageUrl, "Guaraná 1L", productDescription, 40.0);
        product4.setCategory(category2);
        product4.setStore(store1);

        Product product5 = new Product(productImageUrl, "Esfiha de Frango", productDescription, 10.0);
        product5.setCategory(category3);
        product5.setStore(store1);

        Product product6 = new Product(productImageUrl, "Coxinha de Frango", productDescription, 20.0);
        product6.setCategory(category3);
        product6.setStore(store1);

        Product product7 = new Product(productImageUrl, "Pudim de Leite", productDescription, 30.0);
        product7.setCategory(category4);
        product7.setStore(store1);

        Product product8 = new Product(productImageUrl, "Brigadeiro de Colher", productDescription, 40.0);
        product8.setCategory(category4);
        product8.setStore(store1);
        productRepository.saveAll(
                List.of(product1, product2, product3, product4, product5, product6, product7, product8)
        );


        // order-item
        OrderItem orderItem1 = new OrderItem(product1, 2);
        OrderItem orderItem2 = new OrderItem(product2, 3);
        var orderItems = List.of(orderItem1, orderItem2);

        // order
        Order order1 = new Order(
                OrderStatus.PENDENTE,
                OrderPayment.PIX,
                customerUser,
                store1,
                addressCustomerUser
        );
        order1.addOrderItems(orderItems);
        orderRepository.save(order1);
    }
}
