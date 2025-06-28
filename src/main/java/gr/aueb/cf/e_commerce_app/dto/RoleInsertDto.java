package gr.aueb.cf.e_commerce_app.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleInsertDto {

    @NotEmpty(message = "The name must not be empty.")
    private String name;
}
