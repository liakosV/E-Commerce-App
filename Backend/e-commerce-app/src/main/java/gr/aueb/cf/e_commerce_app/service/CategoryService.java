package gr.aueb.cf.e_commerce_app.service;

import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectAlreadyExistsException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectIllegalStateException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.e_commerce_app.dto.CategoryInsertDto;
import gr.aueb.cf.e_commerce_app.dto.CategoryReadOnlyDto;
import gr.aueb.cf.e_commerce_app.mapper.Mapper;
import gr.aueb.cf.e_commerce_app.model.static_data.Category;
import gr.aueb.cf.e_commerce_app.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final Mapper mapper;

    @Transactional(rollbackOn = Exception.class)
    public CategoryReadOnlyDto saveCategory(CategoryInsertDto insertDto) throws AppObjectAlreadyExistsException {

        if (categoryRepository.findByName(insertDto.getName()).isPresent()) {
            throw new AppObjectAlreadyExistsException("Category", "The category with name: " + insertDto.getName() + " already exists");
        }

        Category category = categoryRepository.save(mapper.mapToCategoryEntity(insertDto));

        return mapper.mapToCategoryReadOnlyDto(category);
    }

    @Transactional(rollbackOn = Exception.class)
    public void removeCategory(Long id) throws AppObjectNotFoundException, AppObjectIllegalStateException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppObjectNotFoundException("Category", "The category you want to remove is not found"));

        if (!category.getAllProducts().isEmpty()) throw new AppObjectIllegalStateException("Category", "You cannot remove a category that is used by products");

        categoryRepository.delete(category);
    }

    public List<CategoryReadOnlyDto> getAllCategories() {

        return categoryRepository.findAll(Sort.by("id")).stream()
                .map(mapper::mapToCategoryReadOnlyDto)
                .toList();
    }
}
