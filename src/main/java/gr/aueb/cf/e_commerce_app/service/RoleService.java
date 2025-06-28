package gr.aueb.cf.e_commerce_app.service;

import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectAlreadyExists;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectIllegalState;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectNotFound;
import gr.aueb.cf.e_commerce_app.dto.RoleInsertDto;
import gr.aueb.cf.e_commerce_app.dto.RoleReadOnlyDto;
import gr.aueb.cf.e_commerce_app.mapper.Mapper;
import gr.aueb.cf.e_commerce_app.model.Role;
import gr.aueb.cf.e_commerce_app.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final Mapper mapper;

    public RoleReadOnlyDto createRole(RoleInsertDto roleInsertDto) throws AppObjectAlreadyExists {

        if (roleRepository.findByName(roleInsertDto.getName()).isPresent()) {
            throw new AppObjectAlreadyExists("Role", "Role with the name: " + roleInsertDto.getName() + " already exists");
        }

        Role savedRole = roleRepository.save(mapper.mapToRoleEntity(roleInsertDto));
        return mapper.mapToRoleReadOnlyDto(savedRole);
    }

    public List<RoleReadOnlyDto> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(mapper::mapToRoleReadOnlyDto)
                .toList();
    }

    public void removeRole(Long id) throws AppObjectNotFound, AppObjectIllegalState {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new AppObjectNotFound("Role", "Role with id: " + id + " not found"));

        if (!role.getUsers().isEmpty()) {
            throw new AppObjectIllegalState("Role","Cannot delete a role that is assigned to users");
        }

        roleRepository.delete(role);
    }

}
