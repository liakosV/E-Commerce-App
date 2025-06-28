package gr.aueb.cf.e_commerce_app.rest;

import gr.aueb.cf.e_commerce_app.authentication.AuthenticationService;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectNotAuthorized;
import gr.aueb.cf.e_commerce_app.dto.AuthenticationRequestDto;
import gr.aueb.cf.e_commerce_app.dto.AuthenticationResponseDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @PostMapping("authenticate")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody AuthenticationRequestDto authenticationRequestDto)
        throws AppObjectNotAuthorized {

        AuthenticationResponseDto authenticationResponseDto = authenticationService.authenticate(authenticationRequestDto);
        LOGGER.info("User authenticated");
        return new ResponseEntity<>(authenticationResponseDto, HttpStatus.OK);
    }
}
