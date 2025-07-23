package gr.aueb.cf.e_commerce_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderReadOnlyDto {

    private String uuid;
    private Long orderId;
    private String username;
    private Boolean isActive;
    private List<OrderItemReadOnlyDto> items;
    private BigDecimal totalAmount;
}
