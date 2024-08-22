package br.com.jotave_erref.RestWithSpringBoot.domain.user;

import java.util.Date;

public record TokenData(String username, Boolean authenticated, Date created, Date expiration, String accessToken, String refreshToken) {
}
