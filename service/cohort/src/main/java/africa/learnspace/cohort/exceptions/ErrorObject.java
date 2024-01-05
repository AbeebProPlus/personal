package africa.learnspace.cohort.exceptions;

import java.util.Date;
import lombok.Setter;

@Setter

public class ErrorObject {
    private Integer statusCode;
    private String message;
    private Date timeStamp;
}
