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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final Mapper mapper;

    @Transactional(rollbackOn = Exception.class)
    public OrderReadOnlyDto placeOrder(OrderInsertDto dto) throws AppObjectNotFoundException, AppObjectInvalidArgumentException {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppObjectNotFoundException("Order", "The user was not found"));

        Order order = new Order();
        order.setUser(user);

        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemInsertDto itemInsertDto : dto.getItems()) {
            Product product = productRepository.findById(itemInsertDto.getProductId())
                    .orElseThrow(() -> new AppObjectNotFoundException("Order", "The product was not found"));

            if (itemInsertDto.getQuantity() <= 0) {
                throw new AppObjectInvalidArgumentException("Order", "Quantity must be greater than 0 for product " + product.getName());
            }

            if (product.getQuantity() < itemInsertDto.getQuantity()) {
                throw new AppObjectInvalidArgumentException("Order", "Not enough stock for product: " + product.getName());
            }

            product.setQuantity(product.getQuantity() - itemInsertDto.getQuantity());

            if (product.getQuantity() <= 0) {
                product.setIsActive(false);
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemInsertDto.getQuantity());
            orderItem.setUnitPrice(product.getPrice());
            order.setTotalAmount(orderItem.getTotalPrice());
            orderItem.setOrder(order);

            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        order.setIsActive(true);
        Order saveOrder = orderRepository.save(order);

        return mapper.mapToOrderReadOnlyDto(saveOrder);
    }

    public void deactivateOrder(UUID orderUuid) throws AppObjectNotFoundException {
        Order order = orderRepository.findByUuid(String.valueOf(orderUuid))
                .orElseThrow(() -> new AppObjectNotFoundException("Order", "The order was not found"));

        order.setIsActive(!order.getIsActive());

        orderRepository.save(order);
    }

    public Page<OrderReadOnlyDto> getPaginatedSortedOrders(int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return orderRepository.findAll(pageable).map(mapper::mapToOrderReadOnlyDto);
    }
}
