package africa.learnspace.cohort.data.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateCohortRequest {
    @NotBlank(message = "Required *")
    private String name;

    @NotBlank(message = "Required *")
    private LocalDate startDate;

    @NotBlank(message = "Required *")
    private LocalDate endDate;
}
