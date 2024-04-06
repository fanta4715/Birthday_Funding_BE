package team.haedal.gifticionfunding.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team.haedal.gifticionfunding.dto.common.ResponseDto;
import team.haedal.gifticionfunding.handler.ex.CustomApiException;
import team.haedal.gifticionfunding.handler.ex.CustomValidationException;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<?> apiException(CustomApiException e) {
        return new ResponseEntity<>(
                new ResponseDto<>(-1, e.getMessage(), null),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<?> validationApiException(CustomValidationException e) {
        return new ResponseEntity<>(
                new ResponseDto<>(-1, e.getMessage(), e.getErroMap()),
                HttpStatus.BAD_REQUEST
        );
    }
}
