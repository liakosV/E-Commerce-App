package gr.aueb.cf.e_commerce_app.rest;

import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectAlreadyExistsException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.e_commerce_app.core.exceptions.ValidationException;
import gr.aueb.cf.e_commerce_app.core.filters.ProductFilters;
import gr.aueb.cf.e_commerce_app.dto.ProductInsertDto;
import gr.aueb.cf.e_commerce_app.dto.ProductReadOnlyDto;
import gr.aueb.cf.e_commerce_app.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductRestController.class);
    private final ProductService productService;

    @Operation(
            summary = "Creates a new product",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "New product created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProductReadOnlyDto.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content),
                    @ApiResponse(responseCode = "409", description = "Product already exists", content = @Content),
                    @ApiResponse(responseCode = "404", description = "The category was not found", content = @Content),
                    @ApiResponse(responseCode = "403", description = "You are not authorized to make this call", content = @Content)
            }
    )
    @PostMapping
    public ResponseEntity<ProductReadOnlyDto> saveProduct(@Valid @RequestBody ProductInsertDto insertDto, BindingResult bindingResult)
            throws AppObjectAlreadyExistsException, AppObjectNotFoundException, ValidationException {

        if (bindingResult.hasErrors()) throw new ValidationException(bindingResult);

        ProductReadOnlyDto productReadOnlyDto = productService.saveProduct(insertDto);

        return new ResponseEntity<>(productReadOnlyDto, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Removes a product",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product removed",
                            content = @Content
                    ),
                    @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
            }
    )
    @DeleteMapping("/{uuid}")
    public void removeProduct(@PathVariable String uuid) throws AppObjectNotFoundException {
        productService.removeProduct(uuid);
    }

    @Operation(
            summary = "Get all products filtered and paginated",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Get all products",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProductReadOnlyDto.class)
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<Page<ProductReadOnlyDto>> getProductsFilteredPaginated(
            @ParameterObject ProductFilters filters,
            @ParameterObject Pageable pageable) {
        Page<ProductReadOnlyDto> page = productService.getFilteredPaginatedProducts(filters, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
