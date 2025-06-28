package gr.aueb.cf.e_commerce_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserReadOnlyDto {

    private Long id;

    private String uuid;
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private Boolean isActive;
    private RoleReadOnlyDto role;
    private UserMoreInfoReadOnlyDto userMoreInfo;
}
