package africa.learnspace.trainee.dto;

import africa.learnspace.loan.models.trainee.TraineeLoanDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class LoanPerformanceDto {
    private String instituteName;
    private TraineeLoanDetailDto traineeLoanDetail;
    private List<PaymentDto> paymentHistory;
}
