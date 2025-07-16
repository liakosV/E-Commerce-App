package gr.aueb.cf.e_commerce_app.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInsertDto {

    @NotEmpty(message = "The username must not be empty")
    private String username;

    @NotEmpty(message = "The firstname must not be empty")
    private String firstname;

    @NotEmpty(message = "The lastname must not be empty")
    private String lastname;

    @Pattern(regexp = "^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[0-9])(?=.*?[@#$%!^&*]).{8,}$", message = "Invalid Password")
    private String password;

    @Email(message = "The email is invalid. The email must be like: example@example.com")
    @NotBlank(message = "The email must not be empty")
    private String email;

    @NotNull(message = "The role must not be null")
    private String roleName;

    @Valid
    private UserMoreInfoInsertDto userMoreInfo;
}
