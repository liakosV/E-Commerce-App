package gr.aueb.cf.e_commerce_app.rest;

import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectAlreadyExistsException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectIllegalStateException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.e_commerce_app.core.exceptions.ValidationException;
import gr.aueb.cf.e_commerce_app.dto.CategoryInsertDto;
import gr.aueb.cf.e_commerce_app.dto.CategoryReadOnlyDto;
import gr.aueb.cf.e_commerce_app.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryRestController.class);
    private final CategoryService categoryService;

    @Operation(
            summary = "Creates a new category",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "New category created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CategoryReadOnlyDto.class)
                            )
                    ),
                    @ApiResponse(responseCode = "409", description = "Category already exists", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content)
            }
    )
    @PostMapping
    public ResponseEntity<CategoryReadOnlyDto> saveCategory(@Valid @RequestBody CategoryInsertDto insertDto, BindingResult bindingResult) throws AppObjectAlreadyExistsException, ValidationException {

        if (bindingResult.hasErrors()) throw new ValidationException(bindingResult);

        CategoryReadOnlyDto categoryReadOnlyDto = categoryService.saveCategory(insertDto);
        LOGGER.info("New category added.");
        return new ResponseEntity<>(categoryReadOnlyDto, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Removes a category",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Category removed",
                            content = @Content
                    ),
                    @ApiResponse(responseCode = "404", description = "Category not found", content = @Content),
                    @ApiResponse(responseCode = "409", description = "Cannot remove the category", content = @Content)
            }
    )
    @DeleteMapping("/{id}")
    public void removeCategory(@PathVariable Long id) throws AppObjectNotFoundException, AppObjectIllegalStateException {
        categoryService.removeCategory(id);
        LOGGER.info("Category with id: {} removed.", id);
    }

    @Operation(
            summary = "Get all categories sorted by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Categories found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CategoryReadOnlyDto.class)
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<CategoryReadOnlyDto>> getAllCategories() {
        List<CategoryReadOnlyDto> categoriesList = categoryService.getAllCategories();
        return new ResponseEntity<>(categoriesList, HttpStatus.OK);
    }



}
