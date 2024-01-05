package africa.learnspace.trainee.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class LoanRequestDto {
    private String dateOfBirth;
    private String nextOfKin;

    private String creditWorthinessRating;

    private String fitnessToWorkRating;

    private String educationalBackground;

    private String instituteName;

    private String programName;

    private BigDecimal programCost;


    private BigDecimal initialDeposit;

    private BigDecimal amountRequested;

    private LocalDate referralDate;
}