package gr.aueb.cf.e_commerce_app.rest;

import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectAlreadyExistsException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectIllegalStateException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.e_commerce_app.core.exceptions.ValidationException;
import gr.aueb.cf.e_commerce_app.dto.RoleInsertDto;
import gr.aueb.cf.e_commerce_app.dto.RoleReadOnlyDto;
import gr.aueb.cf.e_commerce_app.service.RoleService;
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
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleRestController.class);
    private final RoleService roleService;

    @Operation(
            summary = "Creates a new role",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Role created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RoleReadOnlyDto.class)
                            )
                    ),
                    @ApiResponse(responseCode = "409", description = "Role already exists", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content)
            }
    )
    @PostMapping("/create")
    public ResponseEntity<RoleReadOnlyDto> createRole(@Valid @RequestBody RoleInsertDto roleInsertDto, BindingResult bindingResult)
            throws AppObjectAlreadyExistsException, ValidationException {

        if (bindingResult.hasErrors()) throw new ValidationException(bindingResult);

        RoleReadOnlyDto roleReadOnlyDto = roleService.createRole(roleInsertDto);
        return new  ResponseEntity<>(roleReadOnlyDto, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Removes a role",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Role removed", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Role not found", content = @Content),
                    @ApiResponse(responseCode = "409", description = "Cannot remove the role", content = @Content)
            }

    )
    @DeleteMapping("remove/{id}")
    public void removeRole(@PathVariable Long id) throws AppObjectNotFoundException, AppObjectIllegalStateException {
        roleService.removeRole(id);
    }

    @Operation(
            summary = "Get all roles sorted by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Roles found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RoleReadOnlyDto.class)
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<RoleReadOnlyDto>> getAllRoles() {
        List<RoleReadOnlyDto> rolesList = roleService.getAllRoles();

        return new ResponseEntity<>(rolesList, HttpStatus.OK);
    }
}
