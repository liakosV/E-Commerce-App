package gr.aueb.cf.e_commerce_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryReadOnlyDto {

    private Long id;
    private String name;
}
