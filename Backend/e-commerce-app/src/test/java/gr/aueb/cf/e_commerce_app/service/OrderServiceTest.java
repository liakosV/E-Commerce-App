package gr.aueb.cf.e_commerce_app.service;

import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectInvalidArgumentException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.e_commerce_app.dto.OrderInsertDto;
import gr.aueb.cf.e_commerce_app.dto.OrderItemInsertDto;
import gr.aueb.cf.e_commerce_app.dto.OrderReadOnlyDto;
import gr.aueb.cf.e_commerce_app.mapper.Mapper;
import gr.aueb.cf.e_commerce_app.model.Order;
import gr.aueb.cf.e_commerce_app.model.OrderItem;
import gr.aueb.cf.e_commerce_app.model.Product;
import gr.aueb.cf.e_commerce_app.model.User;
import gr.aueb.cf.e_commerce_app.repository.OrderRepository;
import gr.aueb.cf.e_commerce_app.repository.ProductRepository;
import gr.aueb.cf.e_commerce_app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private UserRepository userRepository;
    @Mock private ProductRepository productRepository;
    @Mock private Mapper mapper;

    @InjectMocks
    private OrderService orderService;

    private User user;
    private Product product;
    private Order order;
    private OrderItem orderItem;
    private OrderInsertDto orderInsertDto;
    private OrderItemInsertDto orderItemInsertDto;
    private OrderReadOnlyDto readDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testuser");

        product = new Product();
        product.setId(1L);
        product.setName("Mouse");
        product.setQuantity(10);
        product.setPrice(BigDecimal.valueOf(20.0));
        product.setIsActive(true);

        order = new Order();
        order.setUser(user);
        order.setIsActive(true);

        orderItemInsertDto = new OrderItemInsertDto();
        orderItemInsertDto.setProductUuid("1");
        orderItemInsertDto.setQuantity(2);

        orderInsertDto = new OrderInsertDto();
        orderInsertDto.setItems(List.of(orderItemInsertDto));

        readDto = new OrderReadOnlyDto();
//        readDto.setUuid(UUID.randomUUID().toString());
    }

    @Test
    void shouldPlaceOrderSuccessfully() throws Exception {
        // Mock SecurityContext
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("testuser");
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> {
            Order saved = inv.getArgument(0);
            saved.setUuid(UUID.randomUUID().toString());
            return saved;
        });
        when(mapper.mapToOrderReadOnlyDto(any(Order.class))).thenReturn(readDto);

        OrderReadOnlyDto result = orderService.placeOrder(orderInsertDto);

        assertNotNull(result);
        verify(productRepository).findById(1L);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void shouldThrow_WhenUserNotFound() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        assertThrows(AppObjectNotFoundException.class, () -> orderService.placeOrder(orderInsertDto));
    }

    @Test
    void shouldThrow_WhenProductNotFound() {
        mockSecurityUsername("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AppObjectNotFoundException.class, () -> orderService.placeOrder(orderInsertDto));
    }

    @Test
    void shouldThrow_WhenQuantityIsZero() {
        mockSecurityUsername("testuser");
        orderItemInsertDto.setQuantity(0);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(AppObjectInvalidArgumentException.class, () -> orderService.placeOrder(orderInsertDto));
    }

    @Test
    void shouldThrow_WhenNotEnoughStock() {
        mockSecurityUsername("testuser");
        product.setQuantity(1);
        orderItemInsertDto.setQuantity(2);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(AppObjectInvalidArgumentException.class, () -> orderService.placeOrder(orderInsertDto));
    }

    @Test
    void shouldToggleOrderActiveStatus() throws Exception {
        String orderUuid = UUID.randomUUID().toString();
        order.setUuid(orderUuid.toString());
        order.setIsActive(true);

        when(orderRepository.findByUuid(orderUuid.toString())).thenReturn(Optional.of(order));

        orderService.deactivateOrder(orderUuid);

        assertFalse(order.getIsActive());
        verify(orderRepository).save(order);
    }

    @Test
    void shouldThrow_WhenOrderToToggleNotFound() {
        String orderUuid = UUID.randomUUID().toString();
        when(orderRepository.findByUuid(orderUuid)).thenReturn(Optional.empty());

        assertThrows(AppObjectNotFoundException.class, () -> orderService.deactivateOrder(orderUuid));
    }

    @Test
    void shouldReturnPaginatedSortedOrders() {
        Order order2 = new Order();
        Page<Order> page = new PageImpl<>(List.of(order, order2));

        when(orderRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(mapper.mapToOrderReadOnlyDto(any())).thenReturn(readDto);

        Page<OrderReadOnlyDto> result = orderService.getPaginatedSortedOrders(0, 5, "id", "DESC");

        assertEquals(2, result.getTotalElements());
    }

    private void mockSecurityUsername(String username) {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(username);
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);
    }
}

