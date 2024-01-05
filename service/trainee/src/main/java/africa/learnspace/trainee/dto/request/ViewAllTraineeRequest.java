package africa.learnspace.trainee.dto.request;

import lombok.Data;

@Data
public class ViewAllTraineeRequest {
    private String instituteId;
    private String programId;
    private String cohortId;
}
