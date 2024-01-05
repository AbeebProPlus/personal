package africa.learnspace.loan.models.trainee;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String loanId;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private LoanProduct loanProduct;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private LoanRequest loanRequest;
    @OneToOne
    private Trainee trainee;
}