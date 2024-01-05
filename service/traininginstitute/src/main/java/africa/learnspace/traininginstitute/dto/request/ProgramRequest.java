package africa.learnspace.traininginstitute.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProgramRequest {

    @NotNull
    @NotBlank(message = "Name of Program can not be blank")
    private String programName;
    @NotNull
    private String programDescription;
    @NotNull
    private int programDuration;
    @NotNull
    private String deliveryType;
    @NotNull
    private String programMode;
    @NotNull
    private String programType;
    @NotNull
    private String instituteId;

}
