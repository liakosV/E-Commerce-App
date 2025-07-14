package gr.aueb.cf.e_commerce_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductReadOnlyDto {

    private Long id;

    private String uuid;
    private String name;
    private String description;
    private Boolean isActive;
    private Integer quantity;
    private BigDecimal price;
    private CategoryReadOnlyDto category;
}
