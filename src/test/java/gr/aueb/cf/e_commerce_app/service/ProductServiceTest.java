package gr.aueb.cf.e_commerce_app.service;

import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectAlreadyExistsException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.e_commerce_app.dto.ProductInsertDto;
import gr.aueb.cf.e_commerce_app.dto.ProductReadOnlyDto;
import gr.aueb.cf.e_commerce_app.mapper.Mapper;
import gr.aueb.cf.e_commerce_app.model.Product;
import gr.aueb.cf.e_commerce_app.model.static_data.Category;
import gr.aueb.cf.e_commerce_app.repository.CategoryRepository;
import gr.aueb.cf.e_commerce_app.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock private ProductRepository productRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private Mapper mapper;

    @InjectMocks
    private ProductService productService;

    private ProductInsertDto insertDto;
    private Product product;
    private ProductReadOnlyDto readDto;
    private Category category;

    @BeforeEach
    void setUp() {
        insertDto = new ProductInsertDto();
        insertDto.setName("Test Product");
        insertDto.setCategoryId(1L);

        category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setCategory(category);

        readDto = new ProductReadOnlyDto();
        readDto.setName("Test Product");
    }

    @Test
    void shouldSaveProduct_WhenValid() throws Exception {
        when(productRepository.findByName("Test Product")).thenReturn(Optional.empty());
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(mapper.mapToProductEntity(insertDto)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(mapper.mapToProductReadOnlyDto(product)).thenReturn(readDto);

        ProductReadOnlyDto result = productService.saveProduct(insertDto);

        assertEquals("Test Product", result.getName());
    }

    @Test
    void shouldThrow_WhenProductAlreadyExists() {
        when(productRepository.findByName("Test Product")).thenReturn(Optional.of(product));

        assertThrows(AppObjectAlreadyExistsException.class, () -> productService.saveProduct(insertDto));
    }

    @Test
    void shouldThrow_WhenCategoryNotFound() {
        when(productRepository.findByName("Test Product")).thenReturn(Optional.empty());
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AppObjectNotFoundException.class, () -> productService.saveProduct(insertDto));
    }

    @Test
    void shouldReturnPaginatedSortedProducts() {
        Product product2 = new Product();
        product2.setName("Another");

        ProductReadOnlyDto dto2 = new ProductReadOnlyDto();
        dto2.setName("Another");

        Page<Product> page = new PageImpl<>(List.of(product, product2));

        when(productRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(mapper.mapToProductReadOnlyDto(product)).thenReturn(readDto);
        when(mapper.mapToProductReadOnlyDto(product2)).thenReturn(dto2);

        Page<ProductReadOnlyDto> result = productService.getPaginatedSortedProducts(0, 10, "name", "ASC");

        assertEquals(2, result.getTotalElements());
    }

    @Test
    void shouldDeleteProduct_WhenExists() throws Exception {
        when(productRepository.findByUuid("abc-123")).thenReturn(Optional.of(product));

        productService.removeProduct("abc-123");

        verify(productRepository).delete(product);
    }

    @Test
    void shouldThrow_WhenProductToDeleteNotFound() {
        when(productRepository.findByUuid("abc-123")).thenReturn(Optional.empty());

        assertThrows(AppObjectNotFoundException.class, () -> productService.removeProduct("abc-123"));
    }
}
