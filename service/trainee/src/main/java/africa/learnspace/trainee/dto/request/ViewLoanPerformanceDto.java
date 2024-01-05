package africa.learnspace.trainee.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViewLoanPerformanceDto {
    private String traineeId;
    private String instituteId;
    private String cohortId;
    private String programId;
}
