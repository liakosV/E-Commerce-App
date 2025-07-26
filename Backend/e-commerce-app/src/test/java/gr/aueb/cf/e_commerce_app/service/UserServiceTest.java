package gr.aueb.cf.e_commerce_app.service;

import gr.aueb.cf.e_commerce_app.core.enums.Gender;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectAccessDeniedException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectAlreadyExistsException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.e_commerce_app.dto.UserInsertDto;
import gr.aueb.cf.e_commerce_app.dto.UserMoreInfoInsertDto;
import gr.aueb.cf.e_commerce_app.dto.UserReadOnlyDto;
import gr.aueb.cf.e_commerce_app.mapper.Mapper;
import gr.aueb.cf.e_commerce_app.model.User;
import gr.aueb.cf.e_commerce_app.model.UserMoreInfo;
import gr.aueb.cf.e_commerce_app.model.static_data.Region;
import gr.aueb.cf.e_commerce_app.model.static_data.Role;
import gr.aueb.cf.e_commerce_app.repository.RegionRepository;
import gr.aueb.cf.e_commerce_app.repository.RoleRepository;
import gr.aueb.cf.e_commerce_app.repository.UserMoreInfoRepository;
import gr.aueb.cf.e_commerce_app.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private UserMoreInfoRepository userMoreInfoRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private RegionRepository regionRepository;
    @Mock private Mapper mapper;

    @InjectMocks
    private UserService userService;

    private UserInsertDto userInsertDto;
    private Role role;
    private User user;

    private User currentUser;
    private User targetUser;
    private UUID targetUserId;
    private UserMoreInfoInsertDto insertDto;
    private Region region;

    @BeforeEach
    void setUp() {
        userInsertDto = new UserInsertDto();
        userInsertDto.setUsername("john_doe");
        userInsertDto.setEmail("john@example.com");
        userInsertDto.setRoleName("USER");

        role = new Role();
        role.setId(1L);
        role.setName("USER");

        user = new User();
        user.setId(1L);
        user.setUsername("john_doe");
        user.setEmail("john@example.com");
        user.setRole(role);

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                "john_doe", "", List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        currentUser = new User();
        currentUser.setId(1L);
        currentUser.setUsername("john_doe");
        currentUser.setRole(role);

        targetUserId = UUID.randomUUID();
        targetUser = new User();
        targetUser.setId(1L);
        targetUser.setUsername("john_doe");
        targetUser.setUuid(targetUserId.toString());
        targetUser.setRole(role);

        insertDto = new UserMoreInfoInsertDto();
        insertDto.setPhoneNumber("1234567890");
        insertDto.setGender(Gender.MALE);
        insertDto.setAddress("Street");
        insertDto.setAddressNumber("42");
        insertDto.setRegionId(1L);

        region = new Region();
        region.setId(1L);
        region.setName("TestRegion");
    }

    // ----------- saveUser() ------------

    @Test
    void shouldThrow_WhenUsernameExists() {
        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(user));
        assertThrows(AppObjectAlreadyExistsException.class, () -> userService.saveUser(userInsertDto));
    }

    @Test
    void shouldThrow_WhenEmailExists() {
        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        assertThrows(AppObjectAlreadyExistsException.class, () -> userService.saveUser(userInsertDto));
    }

    @Test
    void shouldThrow_WhenRoleNotFound() {
        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());
        when(roleRepository.findByName("USER")).thenReturn(Optional.empty());
        assertThrows(AppObjectNotFoundException.class, () -> userService.saveUser(userInsertDto));
    }

    @Test
    void shouldSaveUser_WhenValid() throws Exception {
        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));
        when(mapper.mapToUserEntity(userInsertDto, role)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        UserReadOnlyDto expectedDto = new UserReadOnlyDto();
        expectedDto.setUsername("john_doe");

        when(mapper.mapToUserReadOnlyDto(user)).thenReturn(expectedDto);

        UserReadOnlyDto result = userService.saveUser(userInsertDto);

        assertEquals("john_doe", result.getUsername());
        assertTrue(user.getIsActive());
    }

    // ----------- deactivateUser() ------------

    @Test
    void deactivateUser_shouldToggleIsActiveFlagAndSave() throws Exception {
        String uuid = "test-uuid";
        Role role = new Role();
        role.setName("admin");
        User user = new User();
        user.setUuid(uuid);
        user.setRole(role);
        user.setIsActive(true);

        when(userRepository.findByUuid(uuid)).thenReturn(Optional.of(user));

        userService.deactivateUser(uuid);

        assertFalse(user.getIsActive());
        verify(userRepository).save(user);
    }

    @Test
    void deactivateUser_shouldThrowExceptionWhenUserNotFound() {
        String uuid = "missing-uuid";
        when(userRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        AppObjectNotFoundException thrown = assertThrows(AppObjectNotFoundException.class, () ->
                userService.deactivateUser(uuid));

        assertEquals("User not found", thrown.getMessage());
        verify(userRepository, never()).save(any());
    }

    // ----------- updateUserMoreInfo() ------------

    @Test
    void shouldThrow_WhenTargetUserNotFound() {
        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(currentUser));
        when(userRepository.findByUuid(targetUserId.toString())).thenReturn(Optional.empty());

        assertThrows(AppObjectNotFoundException.class, () -> userService.updateUserMoreInfo(targetUserId, insertDto));
    }

    @Test
    void shouldThrow_WhenUnauthorizedUserTriesToUpdate() {
        User anotherTargetUser = new User();
        anotherTargetUser.setId(99L);
        anotherTargetUser.setUsername("other");

        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(currentUser));
        when(userRepository.findByUuid(targetUserId.toString())).thenReturn(Optional.of(anotherTargetUser));

        assertThrows(AppObjectAccessDeniedException.class, () -> userService.updateUserMoreInfo(targetUserId, insertDto));
    }

    @Test
    void shouldThrow_WhenPhoneNumberAlreadyUsed() {
        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(currentUser));
        when(userRepository.findByUuid(targetUserId.toString())).thenReturn(Optional.of(targetUser));

        UserMoreInfo existingInfo = new UserMoreInfo();
        User anotherUser = new User();
        anotherUser.setId(999L);
        existingInfo.setUser(anotherUser);

        when(userMoreInfoRepository.findByPhoneNumber(insertDto.getPhoneNumber())).thenReturn(Optional.of(existingInfo));

        assertThrows(AppObjectAlreadyExistsException.class, () -> userService.updateUserMoreInfo(targetUserId, insertDto));
    }

    @Test
    void shouldUpdateUserInfo_WhenValidAndNewInfo() throws Exception {
        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(currentUser));
        when(userRepository.findByUuid(targetUserId.toString())).thenReturn(Optional.of(targetUser));
        when(userMoreInfoRepository.findByPhoneNumber(insertDto.getPhoneNumber())).thenReturn(Optional.empty());
        when(regionRepository.findById(1L)).thenReturn(Optional.of(region));
        when(userRepository.save(any(User.class))).thenReturn(targetUser);
        targetUser.setUserMoreInfo(new UserMoreInfo());

        userService.updateUserMoreInfo(targetUserId, insertDto);

        verify(userRepository).save(targetUser);
        assertEquals("1234567890", targetUser.getUserMoreInfo().getPhoneNumber());
        assertEquals(Gender.MALE, targetUser.getUserMoreInfo().getGender());
        assertEquals("Street", targetUser.getUserMoreInfo().getAddress());
        assertEquals("42", targetUser.getUserMoreInfo().getAddressNumber());
        assertEquals(region, targetUser.getUserMoreInfo().getRegion());
    }

    // ----------- removeUser() ------------

    @Test
    void shouldRemoveUser_WhenUserExistsAndAuthorized() throws Exception {
        String uuid = "some-uuid";
        when(userRepository.findByUuid(uuid)).thenReturn(Optional.of(targetUser));

        userService.removeUser(uuid);

        verify(userRepository).delete(targetUser);
    }

    @Test
    void shouldThrow_WhenRemoveUserAndUserNotFound() {
        String uuid = "missing-uuid";
        when(userRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        assertThrows(AppObjectNotFoundException.class, () -> userService.removeUser(uuid));
    }

    // ----------- getUserByUuid() ------------

    @Test
    void shouldReturnUser_WhenUuidIsValid() throws Exception {
        String uuid = "valid-uuid";
        when(userRepository.findByUuid(uuid)).thenReturn(Optional.of(targetUser));

        UserReadOnlyDto dto = new UserReadOnlyDto();
        dto.setUsername("john_doe");

        when(mapper.mapToUserReadOnlyDto(targetUser)).thenReturn(dto);

        UserReadOnlyDto result = userService.getUserByUuid(uuid);

        assertNotNull(result);
        assertEquals("john_doe", result.getUsername());
    }

    // ----------- getPaginatedSortedUsers() ------------

    @Test
    void shouldReturnPaginatedUsers() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("username").ascending());
        Page<User> page = new PageImpl<>(List.of(user));

        when(userRepository.findAll(any(Pageable.class))).thenReturn(page);
        UserReadOnlyDto dto = new UserReadOnlyDto();
        dto.setUsername("john_doe");
        when(mapper.mapToUserReadOnlyDto(user)).thenReturn(dto);

        Page<UserReadOnlyDto> result = userService.getPaginatedSortedUsers(0, 10, "username", "asc");

        assertEquals(1, result.getContent().size());
        assertEquals("john_doe", result.getContent().get(0).getUsername());
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }
}
