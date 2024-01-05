package africa.learnspace.web.exception;

import africa.learnspace.common.exceptions.ResourceNotFoundException;
import africa.learnspace.financemanager.exception.PortfolioManagerException;
import africa.learnspace.traininginstitute.exception.InstituteProgramException;
import africa.learnspace.web.response.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleLearnSpaceUserManagement(Exception exception) {
        return new ResponseEntity<>(errorResponseBuilder(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PortfolioManagerException.class)
    public ResponseEntity<?> handleLearnSpaceUserManagement(PortfolioManagerException exception) {
        return new ResponseEntity<>(errorResponseBuilder(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    private static ApiError errorResponseBuilder(String message) {
        return ApiError.builder()
                .message(message)
                .timeStamp(LocalDateTime.now())
                .status(false)
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleException(MethodArgumentNotValidException exception) {
        return new ResponseEntity<>(errorResponseBuilder("Invalid Object"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InstituteProgramException.class)
    public  ResponseEntity<Object> handleInstituteProgramException(InstituteProgramException exception){
        return  new ResponseEntity<>(errorResponseBuilder(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public  ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException exception){
        return new ResponseEntity<>(errorResponseBuilder(exception.getMessage()), HttpStatus.NOT_FOUND);
    }
}