package africa.learnspace.loan.models.trainee;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter

public class LoanOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String loanOfferId;
    private String firstName;
    private String lastName;
    private String email;
    private String instituteName;
    private String programName;
    @OneToOne(cascade = CascadeType.ALL)
    private LoanRequest loanRequest;
    @ManyToOne(cascade = CascadeType.ALL)
    private LoanProduct loanProduct;
    private String loanTermsAndConditions;
    private LoanOfferStatus loanOfferStatus;
}
