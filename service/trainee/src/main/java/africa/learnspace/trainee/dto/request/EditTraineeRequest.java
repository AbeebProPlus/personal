package africa.learnspace.trainee.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EditTraineeRequest {
    private String instituteId;
    private String traineeId;
    private String firstName;
    private String lastName;
    @Email(regexp = "^[A-Za-z\\d+_.-]+@[A-Za-z\\d.-]+\\.[A-Za-z]+$")
    private String email;
}
