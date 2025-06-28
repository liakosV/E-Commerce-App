package gr.aueb.cf.e_commerce_app.rest;

import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectAlreadyExists;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectNotFound;
import gr.aueb.cf.e_commerce_app.core.exceptions.ValidationException;
import gr.aueb.cf.e_commerce_app.dto.UserInsertDto;
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
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
                    )
            }
    )
    @PostMapping("/save")
    public ResponseEntity<UserReadOnlyDto> saveUser(@Valid @RequestBody UserInsertDto userInsertDto, BindingResult bindingResult)
            throws ValidationException, AppObjectAlreadyExists, AppObjectNotFound {
//        if (bindingResult.hasErrors()) throw new ValidationException(bindingResult);

        UserReadOnlyDto userReadOnlyDto = userService.saveUser(userInsertDto);
        return new ResponseEntity<>(userReadOnlyDto, HttpStatus.OK);
    }
}
