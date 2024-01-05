package africa.learnspace.trainee.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddTraineeRequest {
    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    @Email(regexp = "^[A-Za-z\\d+_.-]+@[A-Za-z\\d.-]+\\.[A-Za-z]+$")
    private String emailAddress;

    @NotBlank(message = "Program id cannot be blank")
    private String programId;

    @NotBlank(message = "Cohort id cannot be blank")
    private String cohortId;

    @NotBlank(message = "Institute id cannot be blank")
    private String instituteId;
    @NotBlank(message = "Provide fitness to work rating")
    private String fitnessToWorkRating;
    @NotBlank(message = "Provide next of kin")
    private String nextOfKin;
    @NotBlank(message = "Provide educational background")
    private String educationalBackground;
    @NotBlank(message = "Provide date of birth")
    private LocalDate dateOfBirth;
}
