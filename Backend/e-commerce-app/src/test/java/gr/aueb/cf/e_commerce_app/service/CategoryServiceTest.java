package gr.aueb.cf.e_commerce_app.service;

import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectAlreadyExistsException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectIllegalStateException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.e_commerce_app.dto.CategoryInsertDto;
import gr.aueb.cf.e_commerce_app.dto.CategoryReadOnlyDto;
import gr.aueb.cf.e_commerce_app.mapper.Mapper;
import gr.aueb.cf.e_commerce_app.model.static_data.Category;
import gr.aueb.cf.e_commerce_app.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    private CategoryReadOnlyDto readDto;

    @BeforeEach
    void setUp() {
        insertDto = new CategoryInsertDto();
        insertDto.setName("Electronics");

        category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        readDto = new CategoryReadOnlyDto();
        readDto.setId(1L);
        readDto.setName("Electronics");
    }

    @Test
    void saveCategory_ShouldSucceed() throws Exception {
        when(categoryRepository.findByName("Electronics")).thenReturn(Optional.empty());
        when(mapper.mapToCategoryEntity(insertDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(mapper.mapToCategoryReadOnlyDto(category)).thenReturn(readDto);

        CategoryReadOnlyDto result = categoryService.saveCategory(insertDto);

        assertNotNull(result);
        assertEquals("Electronics", result.getName());
        verify(categoryRepository).save(category);
    }

    @Test
    void saveCategory_ShouldThrowIfExists() {
        when(categoryRepository.findByName("Electronics")).thenReturn(Optional.of(category));

        assertThrows(AppObjectAlreadyExistsException.class,
                () -> categoryService.saveCategory(insertDto));
    }

    @Test
    void removeCategory_ShouldSucceed() throws Exception {
        category.setProducts(Set.of()); // empty list
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        categoryService.removeCategory(1L);

        verify(categoryRepository).delete(category);
    }

    @Test
    void removeCategory_ShouldThrowIfNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AppObjectNotFoundException.class,
                () -> categoryService.removeCategory(1L));
    }

    @Test
    void removeCategory_ShouldThrowIfProductsExist() {
        category.setProducts(Set.of(new gr.aueb.cf.e_commerce_app.model.Product()));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        assertThrows(AppObjectIllegalStateException.class,
                () -> categoryService.removeCategory(1L));
    }

    @Test
    void getAllCategories_ShouldReturnSortedList() {
        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Books");

        when(categoryRepository.findAll(Sort.by("id"))).thenReturn(List.of(category, category2));
        when(mapper.mapToCategoryReadOnlyDto(category)).thenReturn(readDto);
        when(mapper.mapToCategoryReadOnlyDto(category2)).thenReturn(new CategoryReadOnlyDto(2L, "Books"));

        List<CategoryReadOnlyDto> result = categoryService.getAllCategories();

        assertEquals(2, result.size());
        assertEquals("Electronics", result.get(0).getName());
        assertEquals("Books", result.get(1).getName());
    }
}

