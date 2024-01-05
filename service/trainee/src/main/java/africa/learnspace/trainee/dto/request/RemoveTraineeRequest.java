package africa.learnspace.trainee.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RemoveTraineeRequest {
    private String traineeId;
    private String instituteId;
}
