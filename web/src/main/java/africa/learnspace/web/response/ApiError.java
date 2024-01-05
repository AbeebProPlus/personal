package africa.learnspace.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
public class ApiError {

    private String message;
    private boolean status;
    private LocalDateTime timeStamp;
}
