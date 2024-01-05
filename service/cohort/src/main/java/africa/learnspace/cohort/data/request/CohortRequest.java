package africa.learnspace.cohort.data.request;


import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class CohortRequest {
    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal tuition;

//    private List<Trainee> trainees;

    private Integer dropouts;

    private String image;

    @Size(max=256)
    private String Description;
}
