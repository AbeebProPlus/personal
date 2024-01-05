package africa.learnspace.financemanager.data.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {

    private Boolean isSuccessful;
    private HttpStatus status;
    private String message;
}
