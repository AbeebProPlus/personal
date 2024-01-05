package africa.learnspace.loanManagement.data.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class CreatLoanOfferRequest {

    private BigDecimal loanAmount;
private BigDecimal loanAmountApproved;
    private double interestRate;
    private  int  tenor;
    private int moratorium;
    private String loanTermsAndConditions;
}
