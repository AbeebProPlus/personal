package africa.learnspace.loanManagement.data.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ViewAllLoanOfferResponse {

   private String firstName;

    private String lastName;

    private String email;

    private String instituteName;

    private String programName;

    private BigDecimal loanAmountApproved;
}
