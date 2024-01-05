package africa.learnspace.cohort.exceptions;

import java.io.Serial;

public class CohortNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public CohortNotFoundException(String message) {
        super(message);
    }
}
