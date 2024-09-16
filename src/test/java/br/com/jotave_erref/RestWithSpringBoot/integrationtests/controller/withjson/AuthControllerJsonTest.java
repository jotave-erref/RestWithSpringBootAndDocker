package br.com.jotave_erref.RestWithSpringBoot.integrationtests.controller.withjson;

import br.com.jotave_erref.RestWithSpringBoot.configs.TestsConfigs;
import br.com.jotave_erref.RestWithSpringBoot.domain.user.TokenData;
import br.com.jotave_erref.RestWithSpringBoot.integrationtests.data.PersonDataTest;
import br.com.jotave_erref.RestWithSpringBoot.integrationtests.data.UserData;
import br.com.jotave_erref.RestWithSpringBoot.integrationtests.testcontainers.AbstractIntegrationsTests;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8888"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthControllerJsonTest extends AbstractIntegrationsTests {
    private TokenData tokenData;

    @Test
    @Order(1)
    void testSignin() throws IOException {

        UserData user = new UserData("jean", "123456");

        tokenData =
                given()
                        .basePath("/auth/signin")
                        .port(TestsConfigs.SERVER_PORT)
                        .contentType(TestsConfigs.CONTENT_TYPE_JSON)
                        .body(user)
                        .when()
                        .post()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .as(TokenData.class);

        assertNotNull(tokenData.accessToken());
        assertNotNull(tokenData.refreshToken());

    }

    @Test
    @Order(2)
    void testRefresh() throws IOException {

        var newTokenData =
                given()
                        .basePath("/auth/refresh")
                        .port(TestsConfigs.SERVER_PORT)
                        .contentType(TestsConfigs.CONTENT_TYPE_JSON)
                        .pathParam("username", tokenData.username())
                        .header(TestsConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenData.refreshToken())
                        .when()
                        .put("{username}")
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .as(TokenData.class);

        assertNotNull(newTokenData.accessToken());
        assertNotNull(newTokenData.refreshToken());

    }
}
