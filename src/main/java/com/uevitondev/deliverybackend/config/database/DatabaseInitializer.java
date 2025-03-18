package com.uevitondev.deliverybackend.config.database;


import com.uevitondev.deliverybackend.domain.address.StoreAddress;
import com.uevitondev.deliverybackend.domain.address.UserAddress;
import com.uevitondev.deliverybackend.domain.address.UserAddressRepository;
import com.uevitondev.deliverybackend.domain.category.Category;
import com.uevitondev.deliverybackend.domain.category.CategoryRepository;
import com.uevitondev.deliverybackend.domain.customer.Customer;
import com.uevitondev.deliverybackend.domain.order.DeliveryAddress;
import com.uevitondev.deliverybackend.domain.order.Order;
import com.uevitondev.deliverybackend.domain.order.OrderRepository;
import com.uevitondev.deliverybackend.domain.order.OrderStatus;
import com.uevitondev.deliverybackend.domain.orderitem.OrderItem;
import com.uevitondev.deliverybackend.domain.payment.PaymentMethod;
import com.uevitondev.deliverybackend.domain.payment.PaymentMethodName;
import com.uevitondev.deliverybackend.domain.payment.PaymentMethodRepository;
import com.uevitondev.deliverybackend.domain.product.Product;
import com.uevitondev.deliverybackend.domain.refreshtoken.RefreshToken;
import com.uevitondev.deliverybackend.domain.refreshtoken.RefreshTokenRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final StoreRepository storeRepository;
    private final OrderRepository orderRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;


    public DatabaseInitializer(
            RoleRepository roleRepository,
            UserRepository userRepository,
            UserAddressRepository userAddressRepository,
            CategoryRepository categoryRepository,

            StoreRepository storeRepository,
            OrderRepository orderRepository,
            PaymentMethodRepository paymentMethodRepository,
            PasswordEncoder passwordEncoder,
            RefreshTokenRepository refreshTokenRepository
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.userAddressRepository = userAddressRepository;
        this.categoryRepository = categoryRepository;
        this.storeRepository = storeRepository;
        this.orderRepository = orderRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepository = refreshTokenRepository;
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
                null,
                "User",
                "Admin",
                "useradmin@gmail.com",
                passwordEncoder.encode("Admin123")
        );
        adminUser.isEnabled(true);
        adminUser.getRoles().add(adminRole);
        userRepository.save(adminUser);

        Customer customerUser = new Customer(
                null,
                "User",
                "Customer",
                "usercustomer@gmail.com",
                passwordEncoder.encode("Customer123")
        );
        customerUser.isEnabled(true);
        customerUser.getRoles().add(customerRole);
        customerUser = userRepository.save(customerUser);

        Seller sellerUser = new Seller(
                null,
                "User",
                "Seller",
                "userseller@gmail.com",
                passwordEncoder.encode("Seller123")

        );
        sellerUser.isEnabled(true);
        sellerUser.getRoles().add(sellerRole);
        sellerUser = userRepository.save(sellerUser);


        // user customer refresh token
        var refreshTokenEntityCustomer = new RefreshToken();
        refreshTokenEntityCustomer.setToken(UUID.randomUUID().toString());
        refreshTokenEntityCustomer.setCreatedAt(LocalDateTime.now());
        refreshTokenEntityCustomer.setUpdatedAt(LocalDateTime.now());
        refreshTokenEntityCustomer.setExpiredAt(LocalDateTime.now().plusSeconds(604800));
        refreshTokenEntityCustomer.setUser(customerUser);
        refreshTokenRepository.save(refreshTokenEntityCustomer);

        // user seller  refresh token
        var refreshTokenEntitySeller = new RefreshToken();
        refreshTokenEntitySeller.setToken(UUID.randomUUID().toString());
        refreshTokenEntitySeller.setCreatedAt(LocalDateTime.now());
        refreshTokenEntitySeller.setUpdatedAt(LocalDateTime.now());
        refreshTokenEntitySeller.setExpiredAt(LocalDateTime.now().plusSeconds(604800));
        refreshTokenEntitySeller.setUser(sellerUser);
        refreshTokenRepository.save(refreshTokenEntitySeller);


        // user customer address
        UserAddress addressCustomerUser = new UserAddress(
                null,
                "User Customer",
                "1199454599985",
                "Rua da flores",
                126,
                "Distrito 1",
                "São Paulo",
                "SP",
                "Condominio Residencial ABA - Apt 26 A",
                "03584000"
        );
        addressCustomerUser.setUser(customerUser);
        addressCustomerUser = userAddressRepository.save(addressCustomerUser);

        customerUser.getAddresses().add(addressCustomerUser);
        userRepository.save(customerUser);


        // category

        final String category1ImgUrl = "https://img.freepik.com/vetores-gratis/slice-pizza-melted-cartoon-vector-icon-ilustracao-alimentacao-icon-objeto-isolado-vector-plano_138676-10750.jpg?t=st=1732815088~exp=1732818688~hmac=98981ac690a813bf2cbe7e0a4a823393342f5afdd9038150eab187ca49963627&w=826";
        final String category2ImgUrl = "https://img.freepik.com/vetores-gratis/modelo-de-design-de-logotipo-kombucha_23-2150038349.jpg?t=st=1732815253~exp=1732818853~hmac=6cc92483eafaeff832d96fb6c3463db17466988eeb40727c545f6bb0bdf7c63c&w=826";
        final String category3ImgUrl = "https://img.freepik.com/vetores-gratis/colecao-de-empanada-assada-deliciosa_23-2148579448.jpg?t=st=1732815316~exp=1732818916~hmac=bd8588b503e08cb44eddb988f14af9ecaafd4d56bf7dff7a0f05e747c0e1104e&w=826";
        final String category4ImgUrl = "https://img.freepik.com/vetores-gratis/coleccao-de-sobremesas-doces-em-estilo-desenhado-a-mao_23-2147783427.jpg?ga=GA1.1.216393060.1732815058&semt=ais_hybrid";

        Category category1 = new Category(null, category1ImgUrl, "PIZZAS");
        Category category2 = new Category(null, category2ImgUrl, "BEBIDAS");
        Category category3 = new Category(null, category3ImgUrl, "SALGADOS");
        Category category4 = new Category(null, category4ImgUrl, "DOCES");
        categoryRepository.saveAll(List.of(category1, category2, category3, category4));

        // address store
        StoreAddress addressStore1 = new StoreAddress(
                null,
                "Pizzaria Sabor",
                "119958796542",
                "Rua da Conservação",
                25,
                "Montes Claros",
                "Minas Gerais",
                "MG",
                "Proximo ao posto de combustível",
                "03484000"
        );

        // store
        var storeLogoUrl = "https://img.freepik.com/fotos-gratis/frente-do-icone-da-" +
                "cesta-de-compras_187299-40115.jpg?t=st=1719251168~exp=17" +
                "19254768~hmac=5ae1185eacbb75d1489105e68614a3caf9777b6f198e48d6db8deb791f933212&w=740";

        Store store1 = new Store(
                null,
                storeLogoUrl,
                "Pizzaria Sabor",
                "119958796542",
                StoreType.PIZZERIA.name(),
                sellerUser,
                addressStore1
        );
        store1.addAddress(addressStore1);
        store1 = storeRepository.save(store1);


        // product
        String productImageUrl = "https://img.freepik.com/vetores-gratis/prato-de-comida-muculmana-de-carneiro_24" +
                "877-82334.jpg?t=st=1722865896~exp=1722869496~hmac=095a28c9295fe6f9c52080ea57ab9a562149ab8" +
                "0737800e0c993b673f3f57f29&w=826";


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
        /*
        productRepository.saveAll(
                List.of(product1, product2, product3, product4, product5, product6, product7, product8)
        );

        */


        // payment method

        var paymentMethod1 = new PaymentMethod(null, PaymentMethodName.DINHEIRO, PaymentMethodName.DINHEIRO.toString());
        var paymentMethod2 = new PaymentMethod(null, PaymentMethodName.PIX, PaymentMethodName.PIX.toString());
        var paymentMethod3 = new PaymentMethod(null, PaymentMethodName.CARTAO, PaymentMethodName.CARTAO.toString());
        paymentMethodRepository.saveAll(List.of(paymentMethod1, paymentMethod2, paymentMethod3));


        // order-item
        OrderItem orderItem1 = new OrderItem(product1, 2, "bem caprichado!");
        OrderItem orderItem2 = new OrderItem(product2, 3, "");
        var orderItems = List.of(orderItem1, orderItem2);

        // order delivery address

        var deliveryAddress = new DeliveryAddress(
                null,
                addressCustomerUser.getName(),
                addressCustomerUser.getPhoneNumber(),
                addressCustomerUser.getStreet(),
                addressCustomerUser.getNumber(),
                addressCustomerUser.getDistrict(),
                addressCustomerUser.getCity(),
                addressCustomerUser.getUf(),
                addressCustomerUser.getComplement(),
                addressCustomerUser.getZipCode(),
                addressCustomerUser.getCreatedAt(),
                addressCustomerUser.getUpdatedAt()
        );

        // order
        Order order1 = new Order(
                null,
                OrderStatus.PENDENTE,
                PaymentMethodName.PIX,
                customerUser,
                store1
        );
        order1.addDeliveryAddress(deliveryAddress);
        order1.addOrderItems(orderItems);
        orderRepository.save(order1);
    }
}
