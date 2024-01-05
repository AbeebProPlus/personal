package africa.learnspace.trainee.dto;

import africa.learnspace.loan.models.trainee.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class TraineeDto {
    private UserDto user;
    private BigDecimal tuitionAmount;
    private BigDecimal initialDeposit;
    private TraineeLoanDetailDto traineeLoanDetail;
    private List<PaymentDto> paymentDto;
    private List<BankDetailDto> bankDetails;
}
