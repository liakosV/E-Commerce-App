package gr.aueb.cf.e_commerce_app.rest;

import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectInvalidArgumentException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.e_commerce_app.dto.OrderInsertDto;
import gr.aueb.cf.e_commerce_app.dto.OrderReadOnlyDto;
import gr.aueb.cf.e_commerce_app.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderRestController.class);
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderReadOnlyDto> placeOrder(@RequestBody OrderInsertDto orderInsertDto)
            throws AppObjectInvalidArgumentException, AppObjectNotFoundException {

        OrderReadOnlyDto orderReadOnlyDto = orderService.placeOrder(orderInsertDto);
        LOGGER.info("Order placed successfully for user {}", orderReadOnlyDto.getUserId());

        return new ResponseEntity<>(orderReadOnlyDto, HttpStatus.CREATED);
    }
}
