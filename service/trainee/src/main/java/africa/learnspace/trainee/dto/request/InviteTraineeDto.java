package africa.learnspace.trainee.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InviteTraineeDto {
    @NotBlank(message = "Institute Id is required")
    private String instituteId;
    @NotBlank(message = "Trainee Email is required")
    private String traineeEmail;
}
