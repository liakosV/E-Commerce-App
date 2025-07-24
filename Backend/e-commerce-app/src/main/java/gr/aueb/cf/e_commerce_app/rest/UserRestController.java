package gr.aueb.cf.e_commerce_app.rest;

import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectAccessDeniedException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectAlreadyExistsException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.e_commerce_app.core.exceptions.ValidationException;
import gr.aueb.cf.e_commerce_app.dto.UserInsertDto;
import gr.aueb.cf.e_commerce_app.dto.UserMoreInfoInsertDto;
import gr.aueb.cf.e_commerce_app.dto.UserMoreInfoReadOnlyDto;
import gr.aueb.cf.e_commerce_app.dto.UserReadOnlyDto;
import gr.aueb.cf.e_commerce_app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRestController.class);
    private final UserService userService;

    @Operation(
           summary = "Get all users paginated and sorted",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                   @ApiResponse(
                           responseCode = "200",
                           description = "Users found",
                           content = @Content(
                                   mediaType = "application/json",
                                   schema = @Schema(implementation = UserReadOnlyDto.class)
                           )
                   ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access Denied",
                            content = @Content
                    )
            }
    )
    @GetMapping
    public ResponseEntity<Page<UserReadOnlyDto>> getPaginatedSortedUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "firstname") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        Page<UserReadOnlyDto> userPage = userService.getPaginatedSortedUsers(page, size, sortBy, sortDirection);
        return new ResponseEntity<>(userPage, HttpStatus.OK);
    }

    @Operation(
            summary = "Save a user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User inserted",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserReadOnlyDto.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "409", description = "User already exists", content = @Content)
            }
    )
    @PostMapping
    public ResponseEntity<UserReadOnlyDto> saveUser(@Valid @RequestBody UserInsertDto userInsertDto, BindingResult bindingResult)
            throws ValidationException, AppObjectAlreadyExistsException, AppObjectNotFoundException {

        if (bindingResult.hasErrors()) throw new ValidationException(bindingResult);

        UserReadOnlyDto userReadOnlyDto = userService.saveUser(userInsertDto);
        LOGGER.info("User inserted successfully.");
        return new ResponseEntity<>(userReadOnlyDto, HttpStatus.OK);
    }

    @Operation(
            summary = "Changes the status of the user to active or inactive",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User status changed", content = @Content),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
            }
    )
    @PatchMapping("/{userUuid}")
    public void deactivateUser(@PathVariable String userUuid) throws AppObjectNotFoundException, AppObjectAccessDeniedException {
        userService.deactivateUser(userUuid);
        LOGGER.info("User status changed");
    }

    @Operation(
            summary = "Deletes a user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User removed", content = @Content),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
            }
    )
    @DeleteMapping("/{userUuid}")
    public void removeUser(@PathVariable String userUuid) throws AppObjectNotFoundException, AppObjectAccessDeniedException {
        userService.removeUser(userUuid);
        LOGGER.info("User has been removed");
    }

    @Operation(
            summary = "Update the userMoreInfo",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "UserMoreInfo updated",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserMoreInfoReadOnlyDto.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "409", description = "Phone number already exists", content = @Content)
            }
    )
    @PutMapping("/{userId}")
    public void updateUserMoreInfo(
            @PathVariable UUID userId,
            @Valid @RequestBody UserMoreInfoInsertDto insertDto,
            BindingResult bindingResult)
            throws AppObjectNotFoundException, AppObjectAlreadyExistsException, AppObjectAccessDeniedException, ValidationException {

        if (bindingResult.hasErrors()) throw new ValidationException(bindingResult);

        userService.updateUserMoreInfo(userId, insertDto);
        LOGGER.info("User infos updated successfully.");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserReadOnlyDto> getUser(@PathVariable String userId) throws AppObjectNotFoundException {
        UserReadOnlyDto userReadOnlyDto = userService.getUserByUuid(userId);
        return new ResponseEntity<>(userReadOnlyDto, HttpStatus.OK);
    }
}
