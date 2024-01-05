package africa.learnspace.loan.models.program;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class ProgramLoanDetail{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String programDetailId;

    private BigDecimal totalAmountDisbursed;

    private BigDecimal totalAmountRepaid;

    private BigDecimal totalOutstanding;

    private double repaymentPercentage;

    private double debtPercentage;
}
