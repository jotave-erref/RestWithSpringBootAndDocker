package br.com.jotave_erref.RestWithSpringBoot.infra;

import br.com.jotave_erref.RestWithSpringBoot.infra.exception.RequiredObjectIsNullException;
import br.com.jotave_erref.RestWithSpringBoot.infra.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    private ResponseEntity<ExceptionResponse> handle(Exception ex, WebRequest request){
        var exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
        return ResponseEntity.internalServerError().body(exceptionResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    private ResponseEntity<ExceptionResponse> handleNotFoundException(Exception ex, WebRequest request){
        var exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(RequiredObjectIsNullException.class)
    private ResponseEntity<ExceptionResponse> handleBadRequestException(Exception ex, WebRequest request){
        var exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    private record ExceptionResponse(Date date, String message, String description) {
    }
}
