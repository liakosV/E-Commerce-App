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
    private RegionReadOnlyDto region;
    private String address;
    private String addressNumber;
}
