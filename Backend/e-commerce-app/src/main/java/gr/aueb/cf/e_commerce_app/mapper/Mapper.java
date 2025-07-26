package gr.aueb.cf.e_commerce_app.mapper;

import gr.aueb.cf.e_commerce_app.dto.*;
import gr.aueb.cf.e_commerce_app.model.*;
import gr.aueb.cf.e_commerce_app.model.static_data.Region;
import gr.aueb.cf.e_commerce_app.model.static_data.Role;
import gr.aueb.cf.e_commerce_app.model.static_data.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Utility component responsible for converting between DTOs and entities.
 * Handles mapping logic for users, roles, products, categories, orders, and related entities.
 */
@Component
@RequiredArgsConstructor
public class Mapper {

    private final PasswordEncoder passwordEncoder;

    /**
     * Converts a {@link User} entity to a {@link UserReadOnlyDto}.
     *
     * @param user the user entity
     * @return the mapped UserReadOnlyDto
     */
    public UserReadOnlyDto mapToUserReadOnlyDto(User user) {
        var dto = new UserReadOnlyDto();

        dto.setId(user.getId());
        dto.setUuid(user.getUuid());
        dto.setUsername(user.getUsername());
        dto.setFirstname(user.getFirstname());
        dto.setLastname(user.getLastname());
        dto.setEmail(user.getEmail());
        dto.setIsActive(user.getIsActive());

        dto.setRole(mapToRoleReadOnlyDto(user.getRole()).getName());

        if (user.getUserMoreInfo() != null) {
            dto.setUserMoreInfo(mapToUserMoreInfoReadOnlyDto(user.getUserMoreInfo()));
        }

        return dto;
    }

    /**
     * Converts a {@link UserInsertDto} to a {@link User} entity, including password encoding and nested info.
     *
     * @param insertDto the insert DTO
     * @param role the role to assign to the user
     * @return the mapped User entity
     */
    public User mapToUserEntity(UserInsertDto insertDto, Role role) {
        User user = new User();

        user.setUsername(insertDto.getUsername());
        user.setFirstname(insertDto.getFirstname());
        user.setLastname(insertDto.getLastname());
        user.setEmail(insertDto.getEmail());
        user.setPassword(passwordEncoder.encode(insertDto.getPassword()));
        user.setRole(role);

        UserMoreInfoInsertDto userMoreInfoInsertDto = insertDto.getUserMoreInfo();
        if (userMoreInfoInsertDto != null) {
            user.setUserMoreInfo(mapToUserMoreInfoEntity(userMoreInfoInsertDto));
        }

        return user;
    }

    /**
     * Converts a {@link Role} entity to a {@link RoleReadOnlyDto}.
     *
     * @param role the role entity
     * @return the mapped RoleReadOnlyDto
     */
    public RoleReadOnlyDto mapToRoleReadOnlyDto(Role role) {
        return new RoleReadOnlyDto(role.getId(), role.getName(), role.getDescription());
    }

    /**
     * Converts a {@link RoleInsertDto} to a {@link Role} entity.
     *
     * @param roleInsertDto the role insert DTO
     * @return the mapped Role entity
     */
    public Role mapToRoleEntity(RoleInsertDto roleInsertDto) {
        Role role = new Role();
        role.setName(roleInsertDto.getName().toUpperCase());
        role.setDescription(roleInsertDto.getDescription());
        return role;
    }

    /**
     * Converts a {@link UserMoreInfoInsertDto} to a {@link UserMoreInfo} entity.
     *
     * @param dto the insert DTO
     * @return the mapped UserMoreInfo entity
     */
    public UserMoreInfo mapToUserMoreInfoEntity(UserMoreInfoInsertDto dto) {
        if (dto == null) return null;

        UserMoreInfo userMoreInfo = new UserMoreInfo();
        userMoreInfo.setGender(dto.getGender());

        if (dto.getRegionId() != null) {
            Region region = new Region();
            region.setId(dto.getRegionId());
            userMoreInfo.setRegion(region);
        }

        userMoreInfo.setAddress(dto.getAddress());
        userMoreInfo.setAddressNumber(dto.getAddressNumber());
        userMoreInfo.setPhoneNumber(dto.getPhoneNumber());

        return userMoreInfo;
    }

    /**
     * Converts a {@link UserMoreInfo} entity to a {@link UserMoreInfoReadOnlyDto}.
     *
     * @param userMoreInfo the entity
     * @return the mapped read-only DTO
     */
    public UserMoreInfoReadOnlyDto mapToUserMoreInfoReadOnlyDto(UserMoreInfo userMoreInfo) {
        if (userMoreInfo == null) return null;

        UserMoreInfoReadOnlyDto dto = new UserMoreInfoReadOnlyDto();
        dto.setPhoneNumber(userMoreInfo.getPhoneNumber());
        dto.setGender(userMoreInfo.getGender());
        dto.setAddress(userMoreInfo.getAddress());
        dto.setAddressNumber(userMoreInfo.getAddressNumber());

        Region region = userMoreInfo.getRegion();
        dto.setRegion(region != null ? mapToRegionReadOnlyDto(region) : null);

        return dto;
    }

    /**
     * Converts a {@link Product} entity to a {@link ProductReadOnlyDto}.
     *
     * @param product the product entity
     * @return the mapped ProductReadOnlyDto
     */
    public ProductReadOnlyDto mapToProductReadOnlyDto(Product product) {
        ProductReadOnlyDto dto = new ProductReadOnlyDto();

        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setUuid(product.getUuid());
        dto.setQuantity(product.getQuantity());
        dto.setPrice(product.getPrice());
        dto.setIsActive(product.getIsActive());
        dto.setCategory(mapToCategoryReadOnlyDto(product.getCategory()));

        return dto;
    }

    /**
     * Converts a {@link ProductInsertDto} to a {@link Product} entity.
     * Also sets {@code isActive} based on the initial quantity.
     *
     * @param insertDto the insert DTO
     * @return the mapped Product entity
     */
    public Product mapToProductEntity(ProductInsertDto insertDto) {
        Product product = new Product();

        product.setName(insertDto.getName());
        product.setDescription(insertDto.getDescription());
        product.setPrice(insertDto.getPrice());
        product.setQuantity(insertDto.getQuantity());
        product.setIsActive(insertDto.getQuantity() > 0);  // Set active based on quantity
        return product;
    }

    /**
     * Converts a {@link CategoryInsertDto} to a {@link Category} entity.
     *
     * @param insertDto the insert DTO
     * @return the mapped Category entity
     */
    public Category mapToCategoryEntity(CategoryInsertDto insertDto) {
        Category category = new Category();
        category.setName(insertDto.getName().substring(0,1).toUpperCase() + insertDto.getName().substring(1).toLowerCase());
        return category;
    }

    /**
     * Converts a {@link Category} entity to a {@link CategoryReadOnlyDto}.
     *
     * @param category the category entity
     * @return the mapped CategoryReadOnlyDto
     */
    public CategoryReadOnlyDto mapToCategoryReadOnlyDto(Category category) {
        return new CategoryReadOnlyDto(category.getId(), category.getName());
    }

    /**
     * Converts an {@link Order} entity to an {@link OrderReadOnlyDto}.
     * Includes item mapping and total calculation.
     *
     * @param order the order entity
     * @return the mapped OrderReadOnlyDto
     */
    public OrderReadOnlyDto mapToOrderReadOnlyDto(Order order) {
        OrderReadOnlyDto dto = new OrderReadOnlyDto();

        List<OrderItemReadOnlyDto> orderItemsDtos = order.getOrderItems().stream()
                .map(this::mapToOrderItemReadOnlyDto)
                .toList();

        BigDecimal totalAmount = orderItemsDtos.stream()
                .map(OrderItemReadOnlyDto::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        dto.setOrderId(order.getId());
        dto.setUsername(order.getUser().getUsername());
        dto.setUuid(order.getUuid());
        dto.setItems(orderItemsDtos);
        dto.setTotalAmount(totalAmount);
        dto.setIsActive(order.getIsActive());

        return dto;
    }

    /**
     * Converts an {@link OrderItem} entity to an {@link OrderItemReadOnlyDto}.
     *
     * @param orderItem the order item entity
     * @return the mapped OrderItemReadOnlyDto
     */
    public OrderItemReadOnlyDto mapToOrderItemReadOnlyDto(OrderItem orderItem) {
        OrderItemReadOnlyDto dto = new OrderItemReadOnlyDto();
        dto.setProductUuid(orderItem.getProduct().getUuid());
        dto.setProductName(orderItem.getProduct().getName());
        dto.setUnitPrice(orderItem.getUnitPrice());
        dto.setQuantity(orderItem.getQuantity());
        dto.setTotalPrice(orderItem.getTotalPrice());
        return dto;
    }

    /**
     * Converts a {@link RegionInsertDto} to a {@link Region} entity.
     *
     * @param dto the insert DTO
     * @return the mapped Region entity
     */
    public Region mapToRegionEntity(RegionInsertDto dto) {
        Region region = new Region();
        region.setName(dto.getName().toUpperCase());
        return region;
    }

    /**
     * Converts a {@link Region} entity to a {@link RegionReadOnlyDto}.
     *
     * @param region the region entity
     * @return the mapped RegionReadOnlyDto
     */
    public RegionReadOnlyDto mapToRegionReadOnlyDto(Region region) {

        return new RegionReadOnlyDto(region.getId(), region.getName());
    }
}