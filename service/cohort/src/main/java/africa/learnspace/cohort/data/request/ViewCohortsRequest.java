package africa.learnspace.cohort.data.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ViewCohortsRequest {
    private int pageNumber;
    private int pageSize;
}
