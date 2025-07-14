package gr.aueb.cf.e_commerce_app.authentication;

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

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthenticationResponseDto authenticate(AuthenticationRequestDto dto) throws AppObjectNotAuthorizedException {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new AppObjectNotAuthorizedException("User", "User not authorized"));

        String token = jwtService.generateToken(authentication.getName(), user.getRole().getName());
        return new AuthenticationResponseDto(user.getFirstname(), user.getLastname(), token);
    }
}
