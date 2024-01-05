package africa.learnspace.loan.models.cohort;

import africa.learnspace.loan.models.program.Program;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Cohort {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String cohortId;

//    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
//    private CohortLoanDetail cohortLoanDetails;

//    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
//    private CohortRepaymentHistory cohortRepaymentHistory;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private String name;

    private BigDecimal tuition;

    private int dropouts;

    private String image;

    @Size(max=2500)
    @Column(length = 2800)
    private String description;

    @ManyToOne
    private Program program;


}