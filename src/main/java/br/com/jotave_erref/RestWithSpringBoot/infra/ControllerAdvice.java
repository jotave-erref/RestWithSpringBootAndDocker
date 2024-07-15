package br.com.jotave_erref.RestWithSpringBoot.infra;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(Exception.class)
    private ResponseEntity<TratarError> error500(Exception ex, WebRequest request){
        var tratadorDeErro = new TratarError(new Date(), ex.getMessage(), request.getDescription(false));
        return ResponseEntity.badRequest().body(tratadorDeErro);
    }

    private record TratarError(Date date, String message, String description) {

    }
}
