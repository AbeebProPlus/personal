package africa.learnspace.loan.models.trainee;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter

public class TraineeLoanDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String traineeLoanDetailId;

    private BigDecimal totalAmountRepaid;

    private BigDecimal totalOutstanding;

    private double repaymentPercentage;

    private double debtPercentage;

    private BigDecimal amountDisbursed;

    @CreationTimestamp
    private LocalDateTime startDate;

    private BigDecimal interestIncurred;

    private BigDecimal monthlyExpectedRepayment;

    private BigDecimal lastActualRepayment;
    @OneToOne
    private Trainee trainee;
}