package africa.learnspace.cohort.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class CohortExceptionHandler {

    @ExceptionHandler(InstituteNotFoundException.class)
    public ResponseEntity<ErrorObject> handleInstituteNotFoundException(InstituteNotFoundException e) {
        return errorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(ProgramNotFoundException.class)
    public ResponseEntity<ErrorObject> handleProgramNotFoundException(ProgramNotFoundException e) {
        return errorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(CohortNotFoundException.class)
    public ResponseEntity<ErrorObject> handleCohortNotFoundException(CohortNotFoundException e) {
        return errorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorObject> handleCohortUpdateException(InvalidInputException e) {
        return errorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    private ResponseEntity<ErrorObject> errorResponse(HttpStatus status, String message) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(status.value());
        errorObject.setMessage(message);
        errorObject.setTimeStamp(new Date());

        return new ResponseEntity<>(errorObject, status);
    }
}

