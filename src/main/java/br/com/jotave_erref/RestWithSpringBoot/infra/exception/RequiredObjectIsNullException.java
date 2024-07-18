package br.com.jotave_erref.RestWithSpringBoot.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequiredObjectIsNullException extends RuntimeException{
    private String msg;
    public RequiredObjectIsNullException(){
        super("It's not allowed to persist a null object");
    }
    public RequiredObjectIsNullException(String msg){
        super(msg);
    }
}
