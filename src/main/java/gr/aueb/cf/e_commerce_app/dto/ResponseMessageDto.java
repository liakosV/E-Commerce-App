package gr.aueb.cf.e_commerce_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMessageDto {
    private String code;
    private String description;

    public ResponseMessageDto(String code) {
        this.code = code;
        this.description = "";
    }
}
