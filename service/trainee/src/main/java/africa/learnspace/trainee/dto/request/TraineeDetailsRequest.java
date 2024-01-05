package africa.learnspace.trainee.dto.request;

import lombok.Data;

@Data
public class TraineeDetailsRequest {
    private String instituteId;
    private String programId;
    private String cohortId;
    private String traineeId;
}
