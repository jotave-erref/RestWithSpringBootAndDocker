package br.com.jotave_erref.RestWithSpringBoot.service;

import br.com.jotave_erref.RestWithSpringBoot.domain.user.TokenData;
import br.com.jotave_erref.RestWithSpringBoot.domain.user.UserData;
import br.com.jotave_erref.RestWithSpringBoot.repository.UserRepository;
import br.com.jotave_erref.RestWithSpringBoot.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private JwtTokenProvider tokenProvider;

        @Autowired
        private UserRepository repository;

        @SuppressWarnings("rawtypes")
        public ResponseEntity signin(UserData data) {
        try {
            var username = data.username();
            var password = data.password();
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            var user = repository.findByUsername(username);

            TokenData tokenResponse;
            if (user != null) {
                tokenResponse = tokenProvider.createAccessToken(username, user.getRoles());
            } else {
                throw new UsernameNotFoundException("Username " + username + " not found!");
            }
            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid username/password supplied!");
        }
    }

        @SuppressWarnings("rawtypes")
        public ResponseEntity refreshToken(String username, String refreshToken) {
        var user = repository.findByUsername(username);

        TokenData tokenResponse;
        if (user != null) {
            tokenResponse = tokenProvider.refreshToken(refreshToken);
        } else {
            throw new UsernameNotFoundException("Username " + username + " not found!");
        }
        return ResponseEntity.ok(tokenResponse);
    }

    public boolean verifyDataIsNotNull(UserData data){
            return data.username() == null || data.username().isBlank()
                    || data.password() == null || data.password().isBlank();
    }

    public boolean verifyrRefreshDataIsNotNull(String username, String refreshToken){
        return username == null || username.isBlank()
                || refreshToken == null || refreshToken.isBlank();
    }
}
