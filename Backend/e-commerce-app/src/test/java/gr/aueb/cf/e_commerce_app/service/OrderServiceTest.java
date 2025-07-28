package gr.aueb.cf.e_commerce_app.service;

import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectInvalidArgumentException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.e_commerce_app.dto.OrderInsertDto;
import gr.aueb.cf.e_commerce_app.dto.OrderItemInsertDto;
import gr.aueb.cf.e_commerce_app.dto.OrderReadOnlyDto;
import gr.aueb.cf.e_commerce_app.mapper.Mapper;
import gr.aueb.cf.e_commerce_app.model.*;
import gr.aueb.cf.e_commerce_app.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private UserRepository userRepository;
    @Mock private ProductRepository productRepository;
    @Mock private Mapper mapper;

    @InjectMocks private OrderService orderService;

    private User mockUser;
    private Product mockProduct;
    private String productUuid;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup mock user
        mockUser = new User();
        mockUser.setUsername("john_doe");

        // Setup mock product
        productUuid = String.valueOf(UUID.randomUUID());
        mockProduct = new Product();
        mockProduct.setUuid(productUuid);
        mockProduct.setName("Test Product");
        mockProduct.setPrice(new BigDecimal("10.00"));
        mockProduct.setQuantity(5);
        mockProduct.setIsActive(true);

        // Setup SecurityContextHolder mock
        Authentication auth = new UsernamePasswordAuthenticationToken("john_doe", null);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void placeOrder_Success() throws Exception {
        // Arrange
        OrderItemInsertDto itemDto = new OrderItemInsertDto();
        itemDto.setProductUuid(productUuid);
        itemDto.setQuantity(2);

        OrderInsertDto orderInsertDto = new OrderInsertDto();
        orderInsertDto.setItems(List.of(itemDto));

        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(mockUser));
        when(productRepository.findByUuid(productUuid)).thenReturn(Optional.of(mockProduct));

        // Simulate saving order
        Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setUser(mockUser);
        savedOrder.setIsActive(true);
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        OrderReadOnlyDto readOnlyDto = new OrderReadOnlyDto();
        readOnlyDto.setOrderId(1L);
        when(mapper.mapToOrderReadOnlyDto(savedOrder)).thenReturn(readOnlyDto);

        // Act
        OrderReadOnlyDto result = orderService.placeOrder(orderInsertDto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getOrderId());

        verify(userRepository).findByUsername("john_doe");
        verify(productRepository).findByUuid(productUuid);
        verify(orderRepository).save(any(Order.class));

        // Verify product quantity updated
        assertEquals(3, mockProduct.getQuantity());
    }

    @Test
    void placeOrder_ThrowsWhenUserNotFound() {
        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.empty());

        OrderInsertDto dto = new OrderInsertDto();

        AppObjectNotFoundException ex = assertThrows(AppObjectNotFoundException.class,
                () -> orderService.placeOrder(dto));

        assertTrue(ex.getMessage().contains("The user was not found"));
    }

    @Test
    void placeOrder_ThrowsWhenProductNotFound() {
        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(mockUser));
        when(productRepository.findByUuid(productUuid)).thenReturn(Optional.empty());

        OrderItemInsertDto itemDto = new OrderItemInsertDto();
        itemDto.setProductUuid(productUuid);
        itemDto.setQuantity(1);

        OrderInsertDto dto = new OrderInsertDto();
        dto.setItems(List.of(itemDto));

        AppObjectNotFoundException ex = assertThrows(AppObjectNotFoundException.class,
                () -> orderService.placeOrder(dto));

        assertTrue(ex.getMessage().contains("The product was not found"));
    }

    @Test
    void placeOrder_ThrowsWhenQuantityZeroOrLess() {
        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(mockUser));
        when(productRepository.findByUuid(productUuid)).thenReturn(Optional.of(mockProduct));

        OrderItemInsertDto itemDto = new OrderItemInsertDto();
        itemDto.setProductUuid(productUuid);
        itemDto.setQuantity(0); // invalid quantity

        OrderInsertDto dto = new OrderInsertDto();
        dto.setItems(List.of(itemDto));

        AppObjectInvalidArgumentException ex = assertThrows(AppObjectInvalidArgumentException.class,
                () -> orderService.placeOrder(dto));

        assertTrue(ex.getMessage().contains("Quantity must be greater than 0"));
    }

    @Test
    void placeOrder_ThrowsWhenNotEnoughStock() {
        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(mockUser));
        when(productRepository.findByUuid(productUuid)).thenReturn(Optional.of(mockProduct));

        OrderItemInsertDto itemDto = new OrderItemInsertDto();
        itemDto.setProductUuid(productUuid);
        itemDto.setQuantity(10); // more than available

        OrderInsertDto dto = new OrderInsertDto();
        dto.setItems(List.of(itemDto));

        AppObjectInvalidArgumentException ex = assertThrows(AppObjectInvalidArgumentException.class,
                () -> orderService.placeOrder(dto));

        assertTrue(ex.getMessage().contains("Not enough stock"));
    }

    @Test
    void deactivateOrder_TogglesIsActive() throws Exception {
        Order order = new Order();
        order.setUuid("order-uuid");
        order.setIsActive(true);

        when(orderRepository.findByUuid("order-uuid")).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        orderService.deactivateOrder("order-uuid");

        assertFalse(order.getIsActive());
        verify(orderRepository).save(order);
    }

    @Test
    void deactivateOrder_ThrowsWhenOrderNotFound() {
        when(orderRepository.findByUuid("order-uuid")).thenReturn(Optional.empty());

        AppObjectNotFoundException ex = assertThrows(AppObjectNotFoundException.class,
                () -> orderService.deactivateOrder("order-uuid"));

        assertTrue(ex.getMessage().contains("The order was not found"));
    }

    @Test
    void removeOrder_DeletesOrder() throws Exception {
        Order order = new Order();
        order.setUuid("order-uuid");

        when(orderRepository.findByUuid("order-uuid")).thenReturn(Optional.of(order));

        orderService.removeOrder("order-uuid");

        verify(orderRepository).delete(order);
    }

    @Test
    void removeOrder_ThrowsWhenOrderNotFound() {
        when(orderRepository.findByUuid("order-uuid")).thenReturn(Optional.empty());

        AppObjectNotFoundException ex = assertThrows(AppObjectNotFoundException.class,
                () -> orderService.removeOrder("order-uuid"));

        assertTrue(ex.getMessage().contains("The order was not found"));
    }

    @Test
    void getPaginatedSortedOrders_ReturnsMappedPage() {
        Order order = new Order();
        order.setId(1L);

        Page<Order> page = new PageImpl<>(List.of(order));
        when(orderRepository.findAll(any(Pageable.class))).thenReturn(page);

        OrderReadOnlyDto dto = new OrderReadOnlyDto();
        dto.setOrderId(1L);

        when(mapper.mapToOrderReadOnlyDto(order)).thenReturn(dto);

        Page<OrderReadOnlyDto> result = orderService.getPaginatedSortedOrders(0, 10, "id", "asc");

        assertEquals(1, result.getContent().size());
        assertEquals(1L, result.getContent().get(0).getOrderId());
    }
}
