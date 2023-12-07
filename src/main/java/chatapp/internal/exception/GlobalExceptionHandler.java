package chatapp.internal.exception;

import chatapp.entity.ErrorResponseEntity;
import chatapp.internal.logger.AppLogger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = AppLogger.getLogger();

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponseEntity> handleAppException(AppException e){
        logger.log(Level.SEVERE, "Error occurred: ", e);
        return ResponseEntity.status(e.getStatusCode())
                .body(ErrorResponseEntity.builder()
                        .statusCode(e.getStatusCode())
                        .message(e.getMessage())
                        .build());
    }
}