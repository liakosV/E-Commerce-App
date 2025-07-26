package gr.aueb.cf.e_commerce_app.service;

import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectAlreadyExistsException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.e_commerce_app.core.filters.ProductFilters;
import gr.aueb.cf.e_commerce_app.dto.ProductInsertDto;
import gr.aueb.cf.e_commerce_app.dto.ProductReadOnlyDto;
import gr.aueb.cf.e_commerce_app.mapper.Mapper;
import gr.aueb.cf.e_commerce_app.model.Product;
import gr.aueb.cf.e_commerce_app.model.static_data.Category;
import gr.aueb.cf.e_commerce_app.repository.CategoryRepository;
import gr.aueb.cf.e_commerce_app.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private Mapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveProduct_success() throws Exception {
        ProductInsertDto insertDto = new ProductInsertDto();
        insertDto.setName("New Product");
        insertDto.setCategoryId(1L);

        Category category = new Category();
        category.setId(1L);

        Product product = new Product();
        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setName("New Product");

        ProductReadOnlyDto readOnlyDto = new ProductReadOnlyDto();
        readOnlyDto.setName("New Product");

        when(productRepository.findByName(insertDto.getName())).thenReturn(Optional.empty());
        when(categoryRepository.findById(insertDto.getCategoryId())).thenReturn(Optional.of(category));
        when(mapper.mapToProductEntity(insertDto)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(savedProduct);
        when(mapper.mapToProductReadOnlyDto(savedProduct)).thenReturn(readOnlyDto);

        ProductReadOnlyDto result = productService.saveProduct(insertDto);

        assertEquals("New Product", result.getName());
        verify(productRepository).save(product);
    }

    @Test
    void saveProduct_shouldThrowAlreadyExists() {
        ProductInsertDto insertDto = new ProductInsertDto();
        insertDto.setName("Existing Product");

        when(productRepository.findByName(insertDto.getName()))
                .thenReturn(Optional.of(new Product()));

        assertThrows(AppObjectAlreadyExistsException.class,
                () -> productService.saveProduct(insertDto));
    }

    @Test
    void saveProduct_shouldThrowCategoryNotFound() {
        ProductInsertDto insertDto = new ProductInsertDto();
        insertDto.setName("Product");
        insertDto.setCategoryId(1L);

        when(productRepository.findByName(insertDto.getName())).thenReturn(Optional.empty());
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AppObjectNotFoundException.class,
                () -> productService.saveProduct(insertDto));
    }

    @Test
    void removeProduct_success() throws Exception {
        Product product = new Product();
        when(productRepository.findByUuid("uuid")).thenReturn(Optional.of(product));

        productService.removeProduct("uuid");

        verify(productRepository).delete(product);
    }

    @Test
    void removeProduct_shouldThrowNotFound() {
        when(productRepository.findByUuid("uuid")).thenReturn(Optional.empty());

        assertThrows(AppObjectNotFoundException.class,
                () -> productService.removeProduct("uuid"));
    }

    @Test
    void getFilteredPaginatedProducts_returnsPage() {
        ProductFilters filters = new ProductFilters(); // all null = no filters
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());

        Product product = new Product();
        product.setName("Sample");

        ProductReadOnlyDto dto = new ProductReadOnlyDto();
        dto.setName("Sample");

        Page<Product> page = new PageImpl<>(List.of(product));
        when(productRepository.findAll((Specification<Product>) any(), eq(pageable))).thenReturn(page);
        when(mapper.mapToProductReadOnlyDto(product)).thenReturn(dto);

        Page<ProductReadOnlyDto> result = productService.getFilteredPaginatedProducts(filters, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Sample", result.getContent().get(0).getName());
    }
}
