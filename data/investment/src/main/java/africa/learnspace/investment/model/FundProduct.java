package africa.learnspace.investment.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class FundProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String fundProductId;

    private String name;

    private double interestRate;

    private Long tenor;

    private String objective;

    private LocalDateTime startDate;

    private BigDecimal size;

    private BigDecimal amountInvested;

    private BigDecimal collected;
    private String createdBy;

//    private BigDecimal incurred;

    private String riskProfile;
//
//    @ManyToOne(cascade = CascadeType.ALL)
//    private Investor investor;
//    @ManyToOne(cascade = CascadeType.ALL)
//    private LoanProduct loanProduct;
}