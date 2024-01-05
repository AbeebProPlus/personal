package africa.learnspace.trainee.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentDto {
    private BigDecimal deposit;
    private String paymentReference;
    private String paymentMethod;
    private LocalDateTime paymentDate;
}
