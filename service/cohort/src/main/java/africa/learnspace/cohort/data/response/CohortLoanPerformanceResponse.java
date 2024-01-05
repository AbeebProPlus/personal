package africa.learnspace.cohort.data.response;

import africa.learnspace.loan.models.cohort.CohortLoanDetail;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CohortLoanPerformanceResponse {
    private String cohortName;

    private BigDecimal totalAmountDisbursed;

    private BigDecimal totalAmountRepaid;

    private BigDecimal totalOutstanding;

    private double repaymentPercentage;

    private double debtPercentage;

    private BigDecimal totalInterestIncurred;

    private BigDecimal monthlyExpected;

    private BigDecimal lastMonthActual;


}
