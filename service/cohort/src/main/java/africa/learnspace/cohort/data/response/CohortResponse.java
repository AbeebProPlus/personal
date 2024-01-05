package africa.learnspace.cohort.data.response;

import africa.learnspace.loan.models.cohort.CohortLoanDetail;
import africa.learnspace.loan.models.program.Program;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


@Setter
@Getter
public class CohortResponse {
    private CohortLoanDetail cohortLoanDetails;
    private LocalDate startDate;
    private LocalDate endDate;
    private String name;
    private BigDecimal tuition;
    private int dropouts;
    private String image;
    private String Description;
    private Program program;

}
