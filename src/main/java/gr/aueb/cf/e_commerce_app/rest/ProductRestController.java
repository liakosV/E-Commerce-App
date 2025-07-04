package gr.aueb.cf.e_commerce_app.rest;

import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectAlreadyExistsException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.e_commerce_app.dto.ProductInsertDto;
import gr.aueb.cf.e_commerce_app.dto.ProductReadOnlyDto;
import gr.aueb.cf.e_commerce_app.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductRestController.class);
    private final ProductService productService;

    @PostMapping("/save")
    public ResponseEntity<ProductReadOnlyDto> saveProduct(@RequestBody ProductInsertDto insertDto) throws AppObjectAlreadyExistsException, AppObjectNotFoundException {

        ProductReadOnlyDto productReadOnlyDto = productService.saveProduct(insertDto);

        return new ResponseEntity<>(productReadOnlyDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<ProductReadOnlyDto>> getPaginatedSortedProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {

        Page<ProductReadOnlyDto> productPage = productService.getPaginatedSortedProducts(page, size, sortBy, sortDirection);

        return new ResponseEntity<>(productPage, HttpStatus.OK);
    }

    @DeleteMapping("/remove/{uuid}")
    public void removeProduct(@PathVariable String uuid) throws AppObjectNotFoundException {
        productService.removeProduct(uuid);
    }

}
