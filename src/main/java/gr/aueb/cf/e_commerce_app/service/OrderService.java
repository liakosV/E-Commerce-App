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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final Mapper mapper;

    @Transactional(rollbackOn = Exception.class)
    public OrderReadOnlyDto placeOrder(OrderInsertDto dto) throws AppObjectNotFoundException, AppObjectInvalidArgumentException {
//        User user = userRepository.findById(dto.getUserId())
//                .orElseThrow(() -> new AppObjectNotFoundException("Order", "The user was not found"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppObjectNotFoundException("Order", "The user was not found"));

        Order order = new Order();
        order.setUser(user);

        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemInsertDto itemInsertDto : dto.getItems()) {
            Product product = productRepository.findById(itemInsertDto.getProductId())
                    .orElseThrow(() -> new AppObjectNotFoundException("Order", "The product was not found"));

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
        Order saveOrder = orderRepository.save(order);

        return mapper.mapToOrderReadOnlyDto(saveOrder);
    }
}
