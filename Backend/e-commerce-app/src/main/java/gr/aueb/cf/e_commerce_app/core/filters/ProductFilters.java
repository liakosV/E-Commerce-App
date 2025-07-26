package gr.aueb.cf.e_commerce_app.core.filters;

import org.springframework.lang.Nullable;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductFilters {

    @Nullable
    private String category;

    @Nullable
    private BigDecimal minPrice;

    @Nullable
    private BigDecimal maxPrice;

    @Nullable
    private Boolean isActive;

    @Nullable
    private String search;

}
