package africa.learnspace.trainee.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReferTraineeRequest {
    @NotBlank
    private String instituteId;
    @NotBlank
    private String traineeId;
    @NotBlank
    private String dateOfBirth;
    @NotBlank
    private String nextOfKin;
    @NotBlank
    private String creditWorthinessRating;
    @NotBlank
    private String fitnessToWorkRating;
    @NotBlank
    private String educationalBackground;
    @NotBlank
    private String referredBy;
    @NotBlank
    private String programName;
    @NotBlank
    private String programCost;
    @NotBlank
    private String initialDeposit;
    @NotBlank
    private String loanAmountRequested;
    @NotBlank
    private String referralDate;
}
