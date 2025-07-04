package gr.aueb.cf.e_commerce_app.rest;

import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectAlreadyExistsException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.e_commerce_app.dto.CategoryInsertDto;
import gr.aueb.cf.e_commerce_app.dto.CategoryReadOnlyDto;
import gr.aueb.cf.e_commerce_app.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryRestController.class);
    private final CategoryService categoryService;

    @PostMapping("/save")
    public ResponseEntity<CategoryReadOnlyDto> saveCategory(@Valid @RequestBody CategoryInsertDto insertDto) throws AppObjectAlreadyExistsException {

        CategoryReadOnlyDto categoryReadOnlyDto = categoryService.saveCategory(insertDto);
        LOGGER.info("New category added.");
        return new ResponseEntity<>(categoryReadOnlyDto, HttpStatus.OK);
    }

    @DeleteMapping("remove/{id}")
    public void removeCategory(@PathVariable Long id) throws AppObjectNotFoundException {
        categoryService.removeCategory(id);
        LOGGER.info("Category with id: {} removed.", id);
    }

}
