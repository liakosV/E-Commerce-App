package gr.aueb.cf.e_commerce_app.dto;

import gr.aueb.cf.e_commerce_app.core.enums.Gender;
import gr.aueb.cf.e_commerce_app.model.static_data.Region;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMoreInfoInsertDto {

    private Gender gender;

    private Long regionId;

    private String address;

    private String addressNumber;

    @Pattern(regexp = "^\\d{10}$", message = "The phone number must be 10 digits.")
    @Nullable
    private String phoneNumber;
}
