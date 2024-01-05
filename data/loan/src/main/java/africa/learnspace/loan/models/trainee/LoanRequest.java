package africa.learnspace.loan.models.trainee;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class LoanRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String loanRequestId;
    private String creditWorthinessRating;
    private String referredBy;
    private BigDecimal loanAmountRequested;
    private LocalDate referralDate;
    @Enumerated(EnumType.STRING)
    private LoanRequestStatus status;
    @OneToOne
    private Trainee trainee;
}