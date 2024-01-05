package africa.learnspace.loan.models.trainee;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Entity
@Getter
@Setter

public class LoanProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String loanProductId;
    private BigDecimal loanAmountApproved;
    private int moratorium;
    private int tenor;
    private double interestRate;
}
