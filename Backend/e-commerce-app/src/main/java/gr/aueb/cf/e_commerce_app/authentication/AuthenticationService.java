package gr.aueb.cf.e_commerce_app.authentication;

import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectAccessDeniedException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectNotAuthorizedException;
import gr.aueb.cf.e_commerce_app.dto.AuthenticationRequestDto;
import gr.aueb.cf.e_commerce_app.dto.AuthenticationResponseDto;
import gr.aueb.cf.e_commerce_app.model.User;
import gr.aueb.cf.e_commerce_app.repository.UserRepository;
import gr.aueb.cf.e_commerce_app.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthenticationResponseDto authenticate(AuthenticationRequestDto dto) throws AppObjectNotAuthorizedException, AppObjectAccessDeniedException {

        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new AppObjectNotAuthorizedException("User", "User not authorized"));

        if (!user.getIsActive()) {
            throw new AppObjectAccessDeniedException("auth", "Your account have been deactivated.");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().getName());
        claims.put("uuid", user.getUuid());

        String token = jwtService.generateToken(authentication.getName(), claims);
        return new AuthenticationResponseDto(user.getFirstname(), user.getLastname(), token);
    }
}
