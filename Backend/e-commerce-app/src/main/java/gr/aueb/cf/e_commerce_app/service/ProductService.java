package gr.aueb.cf.e_commerce_app.service;

import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectAlreadyExistsException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.e_commerce_app.core.filters.ProductFilters;
import gr.aueb.cf.e_commerce_app.core.specifications.ProductSpecification;
import gr.aueb.cf.e_commerce_app.dto.ProductInsertDto;
import gr.aueb.cf.e_commerce_app.dto.ProductReadOnlyDto;
import gr.aueb.cf.e_commerce_app.mapper.Mapper;
import gr.aueb.cf.e_commerce_app.model.Product;
import gr.aueb.cf.e_commerce_app.model.static_data.Category;
import gr.aueb.cf.e_commerce_app.repository.CategoryRepository;
import gr.aueb.cf.e_commerce_app.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final Mapper mapper;

    @Transactional(rollbackOn = Exception.class)
    public ProductReadOnlyDto saveProduct(ProductInsertDto insertDto) throws AppObjectAlreadyExistsException, AppObjectNotFoundException {

        if (productRepository.findByName(insertDto.getName()).isPresent()) {
            throw new AppObjectAlreadyExistsException("Product", "Product with name: " + insertDto.getName() + " already exists");
        }

        Category category = categoryRepository.findById(insertDto.getCategoryId())
                .orElseThrow(() -> new AppObjectNotFoundException("Product", "The category was not found."));

        Product newProduct = productRepository.save(mapper.mapToProductEntity(insertDto));
        newProduct.setCategory(category);

        return mapper.mapToProductReadOnlyDto(newProduct);
    }

    public void removeProduct(String productUuid) throws AppObjectNotFoundException {
        Product product = productRepository.findByUuid(productUuid)
                .orElseThrow(() -> new AppObjectNotFoundException("Product", "The product was not found"));

        productRepository.delete(product);
    }

    @Transactional
    public Page<ProductReadOnlyDto> getFilteredPaginatedProducts(ProductFilters filters, Pageable pageable) {
        return productRepository.findAll(getSpecsFromFilter(filters), pageable)
                .map(mapper::mapToProductReadOnlyDto);
    }

    private Specification<Product> getSpecsFromFilter(ProductFilters filters) {
        return Specification
                .where(ProductSpecification.hasCategory(filters.getCategory()))
                .and(ProductSpecification.hasMinPrice(filters.getMinPrice()))
                .and(ProductSpecification.hasMaxPrice(filters.getMaxPrice()))
                .and(ProductSpecification.isActive(filters.getIsActive()))
                .and(ProductSpecification.hasSearch(filters.getSearch()));
    }
}
