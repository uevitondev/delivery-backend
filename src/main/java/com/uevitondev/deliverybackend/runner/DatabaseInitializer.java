package com.uevitondev.deliverybackend.runner;


import com.uevitondev.deliverybackend.domain.address.Address;
import com.uevitondev.deliverybackend.domain.address.AddressRepository;
import com.uevitondev.deliverybackend.domain.category.Category;
import com.uevitondev.deliverybackend.domain.category.CategoryRepository;
import com.uevitondev.deliverybackend.domain.customer.Customer;
import com.uevitondev.deliverybackend.domain.enums.OrderStatus;
import com.uevitondev.deliverybackend.domain.order.Order;
import com.uevitondev.deliverybackend.domain.order.OrderRepository;
import com.uevitondev.deliverybackend.domain.orderitem.OrderItem;
import com.uevitondev.deliverybackend.domain.product.Product;
import com.uevitondev.deliverybackend.domain.product.ProductRepository;
import com.uevitondev.deliverybackend.domain.role.Role;
import com.uevitondev.deliverybackend.domain.role.RoleRepository;
import com.uevitondev.deliverybackend.domain.seller.Seller;
import com.uevitondev.deliverybackend.domain.store.Store;
import com.uevitondev.deliverybackend.domain.store.StoreRepository;
import com.uevitondev.deliverybackend.domain.user.User;
import com.uevitondev.deliverybackend.domain.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
public class DatabaseInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final OrderRepository orderRepository;


    public DatabaseInitializer(RoleRepository roleRepository, UserRepository userRepository,
                               AddressRepository addressRepository, CategoryRepository categoryRepository,
                               ProductRepository productRepository, StoreRepository storeRepository,
                               OrderRepository orderRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        Role adminRole = new Role("ROLE_ADMIN");
        Role customerRole = new Role("ROLE_CUSTOMER");
        Role sellerRole = new Role("ROLE_SELLER");

        roleRepository.saveAll(List.of(adminRole, customerRole, sellerRole));

        User adminUser = new User();
        adminUser.setId(UUID.randomUUID());
        adminUser.setFirstName("Admin");
        adminUser.setLastName("UserAdmin");
        adminUser.setUsername("admin@gmail.com");
        adminUser.setPassword("$2a$10$Y7fk59/1Pg.ig0Goy0yTS.5RgKD18N5J3MYCo5bPYzVpslJqfr4uu");
        adminUser.getRoles().add(adminRole);
        adminUser = userRepository.save(adminUser);

        Customer customerUser = new Customer();
        customerUser.setId(UUID.randomUUID());
        customerUser.setFirstName("Customer");
        customerUser.setLastName("UserCustomer");
        customerUser.setUsername("customer@gmail.com");
        customerUser.setPassword("$2a$10$Y7fk59/1Pg.ig0Goy0yTS.5RgKD18N5J3MYCo5bPYzVpslJqfr4uu");
        customerUser.getRoles().add(customerRole);
        customerUser = userRepository.save(customerUser);

        Seller sellerUser = new Seller();
        sellerUser.setId(UUID.randomUUID());
        sellerUser.setFirstName("Seller");
        sellerUser.setLastName("UserSeller");
        sellerUser.setUsername("seller@gmail.com");
        sellerUser.setPassword("$2a$10$Y7fk59/1Pg.ig0Goy0yTS.5RgKD18N5J3MYCo5bPYzVpslJqfr4uu");
        sellerUser.getRoles().add(sellerRole);
        sellerUser = userRepository.save(sellerUser);
        //userRepository.saveAll(List.of(adminUser, customerUser, sellerUser));

        // address
        Address addressCustomer1 = new Address("03584000", "SP", "São Paulo", "Distrito 1", "Rua da flores", 126);
        addressCustomer1.setComplement("Condominio Residencial ABA - Apt 26 A");

        Address addressStore1 = new Address("03484000", "MG", "Minas Gerais", "Montes Claros", "Rua da Conservação", 25);
        addressStore1.setComplement("Prédio Comercial, Bloco C, loja 05");
        addressRepository.saveAll(List.of(addressCustomer1, addressStore1));

        // category
        Category category1 = new Category("PIZZAS");
        Category category2 = new Category("BEBIDAS");
        Category category3 = new Category("SALGADOS");
        Category category4 = new Category("DOCES");
        categoryRepository.saveAll(List.of(category1, category2, category3, category4));

        // store
        Store store1 = new Store("Pizzaria Sabor", sellerUser);
        store1.setAddress(addressStore1);
        Store store2 = new Store("Restaurante Villa", sellerUser);
        Store store3 = new Store("Doceria Braga", sellerUser);
        Store store4 = new Store("Adega Bar", sellerUser);
        storeRepository.saveAll(List.of(store1, store2, store3, store4));

        // product
        String productImageUrl = "https://images.pexels.com/photos/6941025/pexels-photo-6941025.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1";
        String productDescription = "Desperte seus sentidos com a irresistível Pizza Suprema, uma obra-prima gastronômica que eleva o prazer de saborear uma boa pizza a um novo patamar.";

        Product product1 = new Product("Greek Pizza", productImageUrl, productDescription, 10.0);
        product1.setCategory(category1);
        product1.setStore(store1);

        Product product2 = new Product("Margherita Pizza", productImageUrl, productDescription, 20.0);
        product2.setCategory(category1);
        product2.setStore(store1);

        Product product3 = new Product("Coca-Cola 2L", productImageUrl, productDescription, 30.0);
        product3.setCategory(category2);
        product3.setStore(store1);

        Product product4 = new Product("Guaraná 1L", productImageUrl, productDescription, 40.0);
        product4.setCategory(category2);
        product4.setStore(store1);

        Product product5 = new Product("Esfiha de Frango", productImageUrl, productDescription, 10.0);
        product5.setCategory(category3);
        product5.setStore(store1);

        Product product6 = new Product("Coxinha de Frango", productImageUrl, productDescription, 20.0);
        product6.setCategory(category3);
        product6.setStore(store1);

        Product product7 = new Product("Pudim de Leite", productImageUrl, productDescription, 30.0);
        product7.setCategory(category4);
        product7.setStore(store1);

        Product product8 = new Product("Brigadeiro de Colher", productImageUrl, productDescription, 40.0);
        product8.setCategory(category4);
        product8.setStore(store1);
        productRepository.saveAll(List.of(product1, product2, product3, product4, product5, product6, product7, product8));

        // order-item
        OrderItem orderItem1 = new OrderItem(2, "obs", product1);
        OrderItem orderItem2 = new OrderItem(1, "obs", product2);

        Set<OrderItem> orderItems = new HashSet<>();
        orderItems.add(orderItem1);
        orderItems.add(orderItem2);

        // order
        Order order1 = new Order(OrderStatus.PENDENTE, customerUser, orderItems);
        order1.setStore(store1);
        order1.setAddress(addressCustomer1);
        orderRepository.save(order1);


    }
}
