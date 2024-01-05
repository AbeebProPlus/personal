package africa.learnspace.investor.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ApiResponse {
    private Object data;
    private boolean isSuccessful;

}
