package gr.aueb.cf.e_commerce_app.service;

import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectAccessDeniedException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectAlreadyExistsException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.e_commerce_app.dto.UserInsertDto;
import gr.aueb.cf.e_commerce_app.dto.UserMoreInfoInsertDto;
import gr.aueb.cf.e_commerce_app.dto.UserReadOnlyDto;
import gr.aueb.cf.e_commerce_app.mapper.Mapper;
import gr.aueb.cf.e_commerce_app.model.static_data.Role;
import gr.aueb.cf.e_commerce_app.model.User;
import gr.aueb.cf.e_commerce_app.model.UserMoreInfo;
import gr.aueb.cf.e_commerce_app.model.static_data.Region;
import gr.aueb.cf.e_commerce_app.repository.RegionRepository;
import gr.aueb.cf.e_commerce_app.repository.RoleRepository;
import gr.aueb.cf.e_commerce_app.repository.UserMoreInfoRepository;
import gr.aueb.cf.e_commerce_app.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMoreInfoRepository userMoreInfoRepository;
    private final RoleRepository roleRepository;
    private final RegionRepository regionRepository;
    private final Mapper mapper;

    @Transactional(rollbackOn = Exception.class)
    public UserReadOnlyDto saveUser(UserInsertDto userInsertDto) throws AppObjectAlreadyExistsException, AppObjectNotFoundException {

        if (userRepository.findByUsername(userInsertDto.getUsername()).isPresent()) {
            throw new AppObjectAlreadyExistsException("User", "User with username: " + userInsertDto.getUsername() + " already exists.");
        }

        if (userRepository.findByEmail(userInsertDto.getEmail()).isPresent()) {
            throw new AppObjectAlreadyExistsException("User", "User with email: " + userInsertDto.getEmail() + " already exists.");
        }

       Role role = roleRepository.findByName(userInsertDto.getRoleName())
               .orElseThrow(() -> new AppObjectNotFoundException("Role", "The Role does not exists"));

        User savedUser = userRepository.save(mapper.mapToUserEntity(userInsertDto, role));



        savedUser.setIsActive(true);
        return mapper.mapToUserReadOnlyDto(savedUser);
    }

    @Transactional(rollbackOn = Exception.class)
    public void updateUserMoreInfo(UUID userId, UserMoreInfoInsertDto insertDto)
            throws AppObjectAlreadyExistsException, AppObjectNotFoundException, AppObjectAccessDeniedException {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username).orElseThrow(() -> new AppObjectNotFoundException("User", "The user for authentication does not found"));

        // Load user
        User user = userRepository.findByUuid(userId.toString())
                .orElseThrow(() -> new AppObjectNotFoundException("User", "User with UUID: " + userId + " not found"));

        boolean isAdmin = currentUser.getRole().getName().equals("ADMIN");
        boolean isOwner = currentUser.getId().equals(user.getId());

        if (!isAdmin && !isOwner) {
            throw new AppObjectAccessDeniedException("UserMoreInfo", "You are not authenticated to update this user's information");
        }

        // Check if phone number is used by someone else
        Optional<UserMoreInfo> existing = userMoreInfoRepository.findByPhoneNumber(insertDto.getPhoneNumber());

        if (existing.isPresent()) {
            User existingUser = existing.get().getUser();
            if (existingUser != null && !existingUser.getId().equals(user.getId())) {
                throw new AppObjectAlreadyExistsException("UserMoreInfo", "Phone number already used by another user");
            }
        }

        UserMoreInfo info = user.getUserMoreInfo();
        if (info == null) {
            info = new UserMoreInfo();
        }

        if (insertDto.getRegion() != null) {
            Region region = regionRepository.findByName(insertDto.getRegion())
                    .orElseThrow(() -> new AppObjectNotFoundException("Region", "The region: " + insertDto.getRegion() + " was not found"));
            info.setRegion(region);
        } else {
            info.setRegion(null);
        }

        if (insertDto.getPhoneNumber() != null) info.setPhoneNumber(insertDto.getPhoneNumber());
        if (insertDto.getGender() != null) info.setGender(insertDto.getGender());
        if (insertDto.getAddress() != null) info.setAddress(insertDto.getAddress());
        if (insertDto.getAddressNumber() != null) info.setAddressNumber(insertDto.getAddressNumber());
        if (insertDto.getProfilePhotoUrl() != null) info.setProfilePhotoUrl(insertDto.getProfilePhotoUrl());

        info.setUser(user);
        user.setUserMoreInfo(info);


        userRepository.save(user);
    }

    public Page<UserReadOnlyDto> getPaginatedSortedUsers(int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return userRepository.findAll(pageable).map(mapper::mapToUserReadOnlyDto);
    }
}
