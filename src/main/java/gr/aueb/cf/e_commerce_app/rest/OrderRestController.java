package gr.aueb.cf.e_commerce_app.rest;

import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectInvalidArgumentException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.e_commerce_app.core.exceptions.ValidationException;
import gr.aueb.cf.e_commerce_app.dto.OrderInsertDto;
import gr.aueb.cf.e_commerce_app.dto.OrderReadOnlyDto;
import gr.aueb.cf.e_commerce_app.dto.UserReadOnlyDto;
import gr.aueb.cf.e_commerce_app.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderRestController.class);
    private final OrderService orderService;

    @Operation(
            summary = "Creates a new order",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "New order created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderReadOnlyDto.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content),
                    @ApiResponse(responseCode = "404", description = "The product not found", content = @Content)
            }
    )
    @PostMapping
    public ResponseEntity<OrderReadOnlyDto> placeOrder(@Valid @RequestBody OrderInsertDto orderInsertDto, BindingResult bindingResult)
            throws AppObjectInvalidArgumentException, AppObjectNotFoundException, ValidationException {

        if (bindingResult.hasErrors()) throw new ValidationException(bindingResult);

        OrderReadOnlyDto orderReadOnlyDto = orderService.placeOrder(orderInsertDto);
        LOGGER.info("Order placed successfully for user {}", orderReadOnlyDto.getUserId());

        return new ResponseEntity<>(orderReadOnlyDto, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Changes the order to active or inactive",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "isActive changed",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Order not found")
            }
    )
    @DeleteMapping("/{orderUuid}")
    public void deactivateOrder(@PathVariable UUID orderUuid) throws AppObjectNotFoundException {
        orderService.deactivateOrder(orderUuid);
        LOGGER.info("Order status changed.");
    }

    @Operation(
            summary = "Get all orders paginated and sorted",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Orders found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderReadOnlyDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access Denied",
                            content = @Content
                    )
            }
    )
    @GetMapping
    public ResponseEntity<Page<OrderReadOnlyDto>> getPaginatedSortedOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "isActive") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        Page<OrderReadOnlyDto> orderPage = orderService.getPaginatedSortedOrders(page, size, sortBy, sortDirection);
        return new ResponseEntity<>(orderPage, HttpStatus.OK);
    }
}
