package gr.aueb.cf.e_commerce_app.service;

import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectAlreadyExistsException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectIllegalStateException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.e_commerce_app.dto.CategoryInsertDto;
import gr.aueb.cf.e_commerce_app.dto.CategoryReadOnlyDto;
import gr.aueb.cf.e_commerce_app.mapper.Mapper;
import gr.aueb.cf.e_commerce_app.model.Product;
import gr.aueb.cf.e_commerce_app.model.static_data.Category;
import gr.aueb.cf.e_commerce_app.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private Mapper mapper;

    @InjectMocks
    private CategoryService categoryService;

    private CategoryInsertDto insertDto;
    private Category category;
    private CategoryReadOnlyDto categoryDto;

    @BeforeEach
    void setUp() {
        insertDto = new CategoryInsertDto();
        insertDto.setName("Electronics");

        category = new Category();
        category.setId(1L);
        category.setName("Electronics");
        category.setProducts(Collections.emptySet());

        categoryDto = new CategoryReadOnlyDto();
        categoryDto.setName("Electronics");
    }

    @Test
    void saveCategory_Saves_WhenNameIsUnique() throws Exception {
        when(categoryRepository.findByName("Electronics")).thenReturn(Optional.empty());
        when(mapper.mapToCategoryEntity(insertDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(mapper.mapToCategoryReadOnlyDto(category)).thenReturn(categoryDto);

        CategoryReadOnlyDto result = categoryService.saveCategory(insertDto);

        assertEquals("Electronics", result.getName());
        verify(categoryRepository).save(category);
    }

    @Test
    void saveCategory_ThrowsException_WhenNameExists() {
        when(categoryRepository.findByName("Electronics")).thenReturn(Optional.of(category));

        assertThrows(AppObjectAlreadyExistsException.class, () -> categoryService.saveCategory(insertDto));
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void removeCategory_Deletes_WhenNoProducts() throws Exception {
        Category category = mock(Category.class);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(category.getAllProducts()).thenReturn(Collections.emptySet());

        categoryService.removeCategory(1L);

        verify(categoryRepository).delete(category);
    }

    @Test
    void removeCategory_ThrowsNotFound_WhenIdDoesNotExist() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AppObjectNotFoundException.class, () -> categoryService.removeCategory(1L));
    }

    @Test
    void removeCategory_ThrowsIllegalState_WhenCategoryHasProducts() {
        Set<Product> products = new HashSet<>();
        products.add(new Product());
        Category category = mock(Category.class);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(category.getAllProducts()).thenReturn(products);

        assertThrows(AppObjectIllegalStateException.class, () -> categoryService.removeCategory(1L));
        verify(categoryRepository, never()).delete((Category) any());
    }

    @Test
    void getAllCategories_ReturnsDtoList() {
        when(categoryRepository.findAll(Sort.by("id"))).thenReturn(List.of(category));
        when(mapper.mapToCategoryReadOnlyDto(category)).thenReturn(categoryDto);

        List<CategoryReadOnlyDto> result = categoryService.getAllCategories();

        assertEquals(1, result.size());
        assertEquals("Electronics", result.get(0).getName());
    }
}
