package gr.aueb.cf.e_commerce_app.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductInsertDto {

    @NotEmpty(message = "The name must not be empty")
    private String name;

    @NotNull(message = "The price must not be null")
    private BigDecimal price;

    @NotEmpty(message = "The description must not be empty")
    private String description;

    @NotNull(message = "The quantity must not be null")
    private Integer quantity;

    @NotNull(message = "Category must not be null")
    private CategoryInsertDto category;
}
