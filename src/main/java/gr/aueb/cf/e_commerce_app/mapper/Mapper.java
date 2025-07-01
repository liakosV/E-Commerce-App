package gr.aueb.cf.e_commerce_app.mapper;

import gr.aueb.cf.e_commerce_app.dto.*;
import gr.aueb.cf.e_commerce_app.model.Product;
import gr.aueb.cf.e_commerce_app.model.Role;
import gr.aueb.cf.e_commerce_app.model.User;
import gr.aueb.cf.e_commerce_app.model.UserMoreInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Mapper {

    private final PasswordEncoder passwordEncoder;

    public UserReadOnlyDto mapToUserReadOnlyDto(User user) {
        var dto = new UserReadOnlyDto();

        dto.setId(user.getId());
        dto.setUuid(user.getUuid());
        dto.setUsername(user.getUsername());
        dto.setFirstname(user.getFirstname());
        dto.setLastname(user.getLastname());
        dto.setEmail(user.getEmail());
        dto.setIsActive(user.getIsActive());

        //map to RoleReadOnlyDto
        dto.setRole(mapToRoleReadOnlyDto(user.getRole()));

        //map to UserMoreInfoDto
        if (user.getUserMoreInfo() != null) {
            dto.setUserMoreInfo(mapToUserMoreInfoReadOnlyDto(user.getUserMoreInfo()));
        }


        return dto;
    }

    public User mapToUserEntity(UserInsertDto insertDto) {
        User user = new User();

        user.setUsername(insertDto.getUsername());
        user.setFirstname(insertDto.getFirstname());
        user.setLastname(insertDto.getLastname());
        user.setEmail(insertDto.getEmail());
        user.setPassword(passwordEncoder.encode(insertDto.getPassword()));
        user.setIsActive(insertDto.getIsActive());
        user.setRole(insertDto.getRole());

        //map to UserMoreInfo
        UserMoreInfoInsertDto userMoreInfoInsertDto = insertDto.getUserMoreInfo();
        if (userMoreInfoInsertDto != null) {
            user.setUserMoreInfo(mapToUserMoreInfoEntity(insertDto.getUserMoreInfo()));
        }


        return user;
    }

    public RoleReadOnlyDto mapToRoleReadOnlyDto(Role role) {
        return new RoleReadOnlyDto(role.getId(), role.getName());
    }

    public Role mapToRoleEntity(RoleInsertDto roleInsertDto) {
        Role role = new Role();
        role.setName(roleInsertDto.getName().toUpperCase());
        return role;
    }

    public UserMoreInfo mapToUserMoreInfoEntity(UserMoreInfoInsertDto dto) {
        if (dto == null) return null;

        UserMoreInfo userMoreInfo = new UserMoreInfo();
        userMoreInfo.setGender(dto.getGender());
        userMoreInfo.setRegion(dto.getRegion());
        userMoreInfo.setAddress(dto.getAddress());
        userMoreInfo.setAddressNumber(dto.getAddressNumber());
        userMoreInfo.setPhoneNumber(dto.getPhoneNumber());
        userMoreInfo.setProfilePhotoUrl(dto.getProfilePhotoUrl());

        return userMoreInfo;
    }

    public UserMoreInfoReadOnlyDto mapToUserMoreInfoReadOnlyDto(UserMoreInfo userMoreInfo) {
        if (userMoreInfo == null) return null;

        UserMoreInfoReadOnlyDto dto = new UserMoreInfoReadOnlyDto();
        dto.setPhoneNumber(userMoreInfo.getPhoneNumber());
        dto.setGender(userMoreInfo.getGender());
        dto.setRegion(userMoreInfo.getRegion());
        dto.setAddress(userMoreInfo.getAddress());
        dto.setAddressNumber(userMoreInfo.getAddressNumber());
        dto.setProfilePhotoUrl(userMoreInfo.getProfilePhotoUrl());

        return dto;
    }

    public ProductReadOnlyDto mapToProductReadOnlyDto(Product product) {
        ProductReadOnlyDto dto = new ProductReadOnlyDto();

        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setUuid(product.getUuid());
        dto.setQuantity(product.getQuantity());
        dto.setPrice(product.getPrice());
        dto.setIsActive(product.getIsActive());

        return dto;
    }

    public Product mapToProductEntity(ProductInsertDto insertDto) {
        Product product = new Product();

        product.setName(insertDto.getName());
        product.setDescription(insertDto.getDescription());
        product.setPrice(insertDto.getPrice());
        product.setQuantity(insertDto.getQuantity());

        return product;
    }
}
