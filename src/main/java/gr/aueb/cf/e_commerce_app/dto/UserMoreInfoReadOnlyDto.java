package gr.aueb.cf.e_commerce_app.dto;

import gr.aueb.cf.e_commerce_app.core.enums.Gender;
import gr.aueb.cf.e_commerce_app.model.static_data.Region;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMoreInfoReadOnlyDto {

    private String phoneNumber;
    private Gender gender;
    private Region region;
    private String address;
    private Integer addressNumber;
    private String profilePhotoUrl;
}
