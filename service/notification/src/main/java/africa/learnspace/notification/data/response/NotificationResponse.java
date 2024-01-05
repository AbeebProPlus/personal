package africa.learnspace.notification.data.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class NotificationResponse {
    private HttpStatus statusCode;
    private int statusCodeValue;
}
