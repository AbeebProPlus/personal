package africa.learnspace.loanManagement.data.response;

import africa.learnspace.loan.models.trainee.LoanProduct;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ViewLoanOfferResponse {
    private String firstName;
    private String lastName;
    private String  email;
    private String instituteName;
    private String programName;
    private BigDecimal loanAmountApproved;
    private LoanProduct loanProduct;
    private double loanInterestRate;
    private int moratorium;
    private int tenor;
    private String loanTermsAndConditions;
}
