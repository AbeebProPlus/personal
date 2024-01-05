package africa.learnspace.cohort.data.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Setter
@Getter
public class CreateCohortResponse {
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
}
