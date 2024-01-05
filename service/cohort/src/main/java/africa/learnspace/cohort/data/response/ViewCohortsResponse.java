package africa.learnspace.cohort.data.response;

import africa.learnspace.loan.models.cohort.Cohort;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ViewCohortsResponse {
    private int pageNumber;
    private int pageSize;
    private long totalElement;
    private int totalPages;
    private boolean last;
    private List<Cohort> content;
}
