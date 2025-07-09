package gr.aueb.cf.e_commerce_app.core;

import gr.aueb.cf.e_commerce_app.core.exceptions.*;
import gr.aueb.cf.e_commerce_app.dto.ResponseMessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String , String>> handleValidationException(ValidationException ex) {
        BindingResult bindingResult = ex.getBindingResult();

        Map<String , String> errors = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AppObjectAccessDeniedException.class})
    public ResponseEntity<ResponseMessageDto> handleConstrainViolationException(AppObjectAccessDeniedException e) {
        return new ResponseEntity<>(new ResponseMessageDto(e.getCode(), e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({AppObjectAlreadyExistsException.class})
    public ResponseEntity<ResponseMessageDto> handleConstraintViolationException(AppObjectAlreadyExistsException e) {
        return new ResponseEntity<>(new ResponseMessageDto(e.getCode(), e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({AppObjectIllegalStateException.class})
    public ResponseEntity<ResponseMessageDto> handleConstraintViolationException(AppObjectIllegalStateException e) {
        return new ResponseEntity<>(new ResponseMessageDto(e.getCode(), e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({AppObjectInvalidArgumentException.class})
    public ResponseEntity<ResponseMessageDto> handleConstraintViolationException(AppObjectInvalidArgumentException e) {
        return new ResponseEntity<>(new ResponseMessageDto(e.getCode(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AppObjectNotAuthorizedException.class})
    public ResponseEntity<ResponseMessageDto> handleConstraintViolationException(AppObjectNotAuthorizedException e) {
        return new ResponseEntity<>(new ResponseMessageDto(e.getCode(), e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({AppObjectNotFoundException.class})
    public ResponseEntity<ResponseMessageDto> handleConstraintViolationException(AppObjectNotFoundException e) {
        return new ResponseEntity<>(new ResponseMessageDto(e.getCode(), e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({AppServerException.class})
    public ResponseEntity<ResponseMessageDto> handleConstraintViolationException(AppServerException e) {
        return new ResponseEntity<>(new ResponseMessageDto(e.getCode(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
