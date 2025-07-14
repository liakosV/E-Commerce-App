package gr.aueb.cf.e_commerce_app.service;

import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectAlreadyExistsException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectIllegalStateException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.e_commerce_app.dto.RoleInsertDto;
import gr.aueb.cf.e_commerce_app.dto.RoleReadOnlyDto;
import gr.aueb.cf.e_commerce_app.mapper.Mapper;
import gr.aueb.cf.e_commerce_app.model.static_data.Role;
import gr.aueb.cf.e_commerce_app.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final Mapper mapper;

    @Transactional(rollbackOn = Exception.class)
    public RoleReadOnlyDto createRole(RoleInsertDto roleInsertDto) throws AppObjectAlreadyExistsException {

        if (roleRepository.findByName(roleInsertDto.getName()).isPresent()) {
            throw new AppObjectAlreadyExistsException("Role", "Role with the name: " + roleInsertDto.getName() + " already exists");
        }

        Role savedRole = roleRepository.save(mapper.mapToRoleEntity(roleInsertDto));
        return mapper.mapToRoleReadOnlyDto(savedRole);
    }

    @Transactional(rollbackOn = Exception.class)
    public void removeRole(Long id) throws AppObjectNotFoundException, AppObjectIllegalStateException {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new AppObjectNotFoundException("Role", "Role with id: " + id + " not found"));

        if (!role.getUsers().isEmpty()) {
            throw new AppObjectIllegalStateException("Role","Cannot delete a role that is assigned to users");
        }

        roleRepository.delete(role);
    }

    public List<RoleReadOnlyDto> getAllRoles() {

        return roleRepository.findAll(Sort.by("id")).stream()
                .map(mapper::mapToRoleReadOnlyDto)
                .toList();
    }

}
