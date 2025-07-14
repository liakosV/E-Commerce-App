package gr.aueb.cf.e_commerce_app.dto;

import gr.aueb.cf.e_commerce_app.core.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMoreInfoReadOnlyDto {

    private String phoneNumber;
    private Gender gender;
    private String regionName;
    private String address;
    private String addressNumber;
    private String profilePhotoUrl;
}
