package gr.aueb.cf.e_commerce_app.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionInsertDto {

    @NotNull(message = "The name must not be null")
    private String name;
}
