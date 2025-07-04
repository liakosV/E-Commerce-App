package gr.aueb.cf.e_commerce_app.rest;

import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectAlreadyExistsException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectIllegalStateException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.e_commerce_app.dto.RoleInsertDto;
import gr.aueb.cf.e_commerce_app.dto.RoleReadOnlyDto;
import gr.aueb.cf.e_commerce_app.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleRestController.class);
    private final RoleService roleService;

    @PostMapping("/create")
    public ResponseEntity<RoleReadOnlyDto> createRole(@RequestBody RoleInsertDto roleInsertDto) throws AppObjectAlreadyExistsException {
        RoleReadOnlyDto roleReadOnlyDto = roleService.createRole(roleInsertDto);
        return new  ResponseEntity<>(roleReadOnlyDto, HttpStatus.CREATED);
    }

    @DeleteMapping("remove/{id}")
    public void removeRole(@PathVariable Long id) throws AppObjectNotFoundException, AppObjectIllegalStateException {
        roleService.removeRole(id);
    }
}
