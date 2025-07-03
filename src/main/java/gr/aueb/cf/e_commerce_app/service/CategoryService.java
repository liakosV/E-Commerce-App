package gr.aueb.cf.e_commerce_app.service;

import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectAlreadyExists;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectNotFound;
import gr.aueb.cf.e_commerce_app.dto.CategoryInsertDto;
import gr.aueb.cf.e_commerce_app.dto.CategoryReadOnlyDto;
import gr.aueb.cf.e_commerce_app.mapper.Mapper;
import gr.aueb.cf.e_commerce_app.model.static_data.Category;
import gr.aueb.cf.e_commerce_app.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final Mapper mapper;

    @Transactional(rollbackOn = Exception.class)
    public CategoryReadOnlyDto saveCategory(CategoryInsertDto insertDto) throws AppObjectAlreadyExists {

        if (categoryRepository.findByName(insertDto.getName()).isPresent()) {
            throw new AppObjectAlreadyExists("Category", "The category with name: " + insertDto.getName() + " already exists");
        }

        Category category = categoryRepository.save(mapper.mapToCategoryEntity(insertDto));

        return mapper.mapToCategoryReadOnlyDto(category);
    }

    @Transactional(rollbackOn = Exception.class)
    public void removeCategory(Long id) throws AppObjectNotFound {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppObjectNotFound("Category", "The category you want to remove is not found"));

        categoryRepository.delete(category);
    }
}
