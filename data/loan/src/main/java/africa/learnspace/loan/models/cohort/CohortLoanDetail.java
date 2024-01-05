package africa.learnspace.loan.models.cohort;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CohortLoanDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String cohortDetailId;

    private BigDecimal totalAmountDisbursed;

    private BigDecimal totalAmountRepaid;

    private BigDecimal totalOutstanding;

    private double repaymentPercentage;

    private double debtPercentage;

    private BigDecimal totalInterestIncurred;
    private BigDecimal monthlyExpected;
    private BigDecimal lastMonthActual;

    @OneToOne
    private Cohort cohort;
}
