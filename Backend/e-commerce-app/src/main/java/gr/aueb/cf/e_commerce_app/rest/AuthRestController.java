package gr.aueb.cf.e_commerce_app.rest;

import gr.aueb.cf.e_commerce_app.authentication.AuthenticationService;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectAccessDeniedException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectNotAuthorizedException;
import gr.aueb.cf.e_commerce_app.core.exceptions.ValidationException;
import gr.aueb.cf.e_commerce_app.dto.AuthenticationRequestDto;
import gr.aueb.cf.e_commerce_app.dto.AuthenticationResponseDto;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthRestController.class);
    private final AuthenticationService authenticationService;

    @Operation(
            summary = "Authenticate a user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User authenticated",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticationResponseDto.class)
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "Not authorized", content = @Content)
            }
    )
    @PostMapping("authenticate")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@Valid @RequestBody AuthenticationRequestDto authenticationRequestDto, BindingResult bindingResult)
            throws AppObjectNotAuthorizedException, AppObjectAccessDeniedException, ValidationException {
        if (bindingResult.hasErrors()) throw new ValidationException(bindingResult);

        AuthenticationResponseDto authenticationResponseDto = authenticationService.authenticate(authenticationRequestDto);
        LOGGER.info("User authenticated");
        return new ResponseEntity<>(authenticationResponseDto, HttpStatus.OK);
    }
}
