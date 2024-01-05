package africa.learnspace.cohort.exceptions;

import java.io.Serial;

public class InstituteNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public InstituteNotFoundException(String message) {
        super(message);
    }
}
