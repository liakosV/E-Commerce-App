package gr.aueb.cf.e_commerce_app.rest;

import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectAlreadyExists;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectNotFound;
import gr.aueb.cf.e_commerce_app.dto.ProductInsertDto;
import gr.aueb.cf.e_commerce_app.dto.ProductReadOnlyDto;
import gr.aueb.cf.e_commerce_app.mapper.Mapper;
import gr.aueb.cf.e_commerce_app.model.Product;
import gr.aueb.cf.e_commerce_app.repository.ProductRepository;
import gr.aueb.cf.e_commerce_app.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductRestController.class);
    private final ProductService productService;

    @PostMapping("/save")
    public ResponseEntity<ProductReadOnlyDto> saveProduct(@RequestBody ProductInsertDto insertDto) throws AppObjectAlreadyExists, AppObjectNotFound {

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
    public void removeProduct(@PathVariable String uuid) throws AppObjectNotFound {
        productService.removeProduct(uuid);
    }

}
