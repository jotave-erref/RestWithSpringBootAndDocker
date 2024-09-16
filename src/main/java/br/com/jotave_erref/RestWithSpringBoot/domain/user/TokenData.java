package br.com.jotave_erref.RestWithSpringBoot.domain.user;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Date;

@XmlRootElement
public record TokenData(String username, Boolean authenticated, Date created, Date expiration, String accessToken, String refreshToken) {
}
