package africa.learnspace.trainee.dto;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TraineeLoanDetailDto {
    private BigDecimal totalAmountRepaid;

    private BigDecimal totalOutstanding;

    private double repaymentPercentage;

    private double debtPercentage;

    private BigDecimal amountDisbursed;

    private LocalDateTime startDate;

    private BigDecimal interestIncurred;

    private BigDecimal monthlyExpectedRepayment;

    private BigDecimal lastActualRepayment;
}