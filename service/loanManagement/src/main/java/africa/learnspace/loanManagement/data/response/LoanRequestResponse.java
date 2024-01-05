package africa.learnspace.loanManagement.data.response;

import africa.learnspace.loan.models.trainee.LoanRequestStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
public class LoanRequestResponse {

   private String firstName;
    private String lastName;
    private String email;
    private String dateOfBirth;
    private String nextOfKin;
    private String creditWorthinessRating;
    private String fitnessToWorkRating;
    private String EducationalBackground;
    private String InstituteName;
    private String programName;
    private String programCost;
    private LocalDate programStartDate;
    private BigDecimal initialDepositLoan;
    private BigDecimal amountRequested;
    private LocalDate referralDate;
    private boolean identityVerified;
    private LoanRequestStatus loanRequestStatus;
}
