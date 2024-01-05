package africa.learnspace.common.responses;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class PaginatedResponse<T> {

    public List<T> content;

    public int pageNo;

    public int pageSize;

    public int total;

    private boolean last;
}