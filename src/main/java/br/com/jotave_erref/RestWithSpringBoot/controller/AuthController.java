package br.com.jotave_erref.RestWithSpringBoot.controller;

import br.com.jotave_erref.RestWithSpringBoot.domain.user.UserData;
import br.com.jotave_erref.RestWithSpringBoot.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @PostMapping("/signin")
    @Operation(summary = "Authenticates a user and returns a token")
    @Transactional
    public ResponseEntity signin(@RequestBody UserData data){
        if(service.verifyDataIsNotNull(data))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Client Request!");
        var token = service.signin(data);
        if(token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Client Request!");
        return token;
    }

    @PutMapping("/refresh/{username}")
    @Operation(summary = "Refresh token for authenticated user and returns a token")
    @Transactional
    public ResponseEntity signin(@PathVariable("username") String username, @RequestHeader("Authorization") String refreshToken){
        if(service.verifyrRefreshDataIsNotNull(username, refreshToken))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Client Request!");
        var token = service.refreshToken(username, refreshToken);
        if(token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Client Request!");
        return token;
    }
}
