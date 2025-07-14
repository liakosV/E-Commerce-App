package gr.aueb.cf.e_commerce_app.service;

import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectAlreadyExistsException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectIllegalStateException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.e_commerce_app.dto.RoleInsertDto;
import gr.aueb.cf.e_commerce_app.dto.RoleReadOnlyDto;
import gr.aueb.cf.e_commerce_app.mapper.Mapper;
import gr.aueb.cf.e_commerce_app.model.User;
import gr.aueb.cf.e_commerce_app.model.static_data.Role;
import gr.aueb.cf.e_commerce_app.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock private RoleRepository roleRepository;
    @Mock private Mapper mapper;

    @InjectMocks
    private RoleService roleService;

    private RoleInsertDto roleInsertDto;
    private Role role;
    private RoleReadOnlyDto roleReadDto;

    @BeforeEach
    void setUp() {
        roleInsertDto = new RoleInsertDto();
        roleInsertDto.setName("ADMIN");

        role = new Role();
        role.setId(1L);
        role.setName("ADMIN");

        roleReadDto = new RoleReadOnlyDto();
        roleReadDto.setName("ADMIN");
    }

    @Test
    void shouldCreateRole_WhenNotExists() throws Exception {
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.empty());
        when(mapper.mapToRoleEntity(roleInsertDto)).thenReturn(role);
        when(roleRepository.save(role)).thenReturn(role);
        when(mapper.mapToRoleReadOnlyDto(role)).thenReturn(roleReadDto);

        RoleReadOnlyDto result = roleService.createRole(roleInsertDto);

        assertEquals("ADMIN", result.getName());
    }

    @Test
    void shouldThrow_WhenRoleAlreadyExists() {
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(role));

        assertThrows(AppObjectAlreadyExistsException.class, () -> roleService.createRole(roleInsertDto));
    }

    @Test
    void shouldDeleteRole_WhenNoUsersAssigned() throws Exception {
        role.setUsers(Collections.emptySet());

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        roleService.removeRole(1L);

        verify(roleRepository).delete(role);
    }

    @Test
    void shouldThrow_WhenRoleNotFoundToDelete() {
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AppObjectNotFoundException.class, () -> roleService.removeRole(1L));
    }

    @Test
    void shouldThrow_WhenRoleHasUsers() {
        User user = new User();
        role.setUsers(Set.of(user));

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        assertThrows(AppObjectIllegalStateException.class, () -> roleService.removeRole(1L));
    }

    @Test
    void shouldReturnAllRoles() {
        Role role1 = new Role();
        role1.setId(1L);
        role1.setName("ADMIN");

        Role role2 = new Role();
        role2.setId(2L);
        role2.setName("USER");

        RoleReadOnlyDto dto1 = new RoleReadOnlyDto();
        dto1.setName("ADMIN");

        RoleReadOnlyDto dto2 = new RoleReadOnlyDto();
        dto2.setName("USER");

        when(roleRepository.findAll(Sort.by("id"))).thenReturn(List.of(role1, role2));
        when(mapper.mapToRoleReadOnlyDto(role1)).thenReturn(dto1);
        when(mapper.mapToRoleReadOnlyDto(role2)).thenReturn(dto2);

        List<RoleReadOnlyDto> result = roleService.getAllRoles();

        assertEquals(2, result.size());
        assertEquals("ADMIN", result.get(0).getName());
        assertEquals("USER", result.get(1).getName());
    }
}