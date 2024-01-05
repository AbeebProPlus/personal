package africa.learnspace.cohort.exceptions;

import java.io.Serial;

public class ProgramNotFoundException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1L;

    public ProgramNotFoundException(String message) {
        super(message);
    }

}
