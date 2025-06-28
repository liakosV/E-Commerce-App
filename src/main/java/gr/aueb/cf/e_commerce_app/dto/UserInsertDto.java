package gr.aueb.cf.e_commerce_app.dto;

import gr.aueb.cf.e_commerce_app.model.Role;
import gr.aueb.cf.e_commerce_app.model.UserMoreInfo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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

    @Email(message = "The email is invalid")
    private String email;

    @NotNull(message = "The role must not be null")
    private Role role;

    @NotNull(message = "Is active must not be null")
    private Boolean isActive;

    private UserMoreInfoInsertDto userMoreInfo;
}
