package africa.learnspace.loanManagement.data.response;


import africa.learnspace.loan.models.trainee.LoanRequestStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
public class ViewAllLoanResponse {

    private String firstName;
    private String lastName;
    private String instituteName;
    private String ProgramName;
    private LocalDate ProgramStartDate;
    private BigDecimal amountRequested;
    private LocalDate referralDate;
    private boolean identityVerified;
    private LoanRequestStatus loanRequestStatus;
}
