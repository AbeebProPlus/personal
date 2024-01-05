package africa.learnspace.loanManagement.utils;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiResponse {
    private Object data;
    private boolean isSuccessful;

}
