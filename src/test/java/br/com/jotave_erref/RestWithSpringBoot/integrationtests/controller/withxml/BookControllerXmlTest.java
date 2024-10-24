package br.com.jotave_erref.RestWithSpringBoot.integrationtests.controller.withxml;

import br.com.jotave_erref.RestWithSpringBoot.configs.TestsConfigs;
import br.com.jotave_erref.RestWithSpringBoot.integrationtests.data.BookDataTest;
import br.com.jotave_erref.RestWithSpringBoot.integrationtests.data.TokenDataTest;
import br.com.jotave_erref.RestWithSpringBoot.integrationtests.data.UserData;
import br.com.jotave_erref.RestWithSpringBoot.integrationtests.testcontainers.AbstractIntegrationsTests;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8888"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerXmlTest extends AbstractIntegrationsTests {

    private static RequestSpecification specification;
    private static ObjectMapper mapper;
    private static BookDataTest book;

    @BeforeAll
    public static void setUp(){
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        book = new BookDataTest();
    }

    @Test
    @Order(0)
    void authotization() throws IOException {
        UserData user = new UserData("jean", "123456");

        var tokenData =
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
                        .as(TokenDataTest.class)
                        .getAccessToken();

        specification = new RequestSpecBuilder()
                .addHeader(TestsConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenData)
                .setBasePath("/api/books")
                .setPort(TestsConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @Order(1)
    void testCreateBook() throws IOException {
        mockBook();

        var content =
                given().spec(specification)
                        .contentType(TestsConfigs.CONTENT_TYPE_JSON)
                        .body(book)
                        .when()
                        .post()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .asString();
        BookDataTest createBook = mapper.readValue(content, BookDataTest.class);
//        book = createBook;

        System.out.println("book " + book);
        System.out.println("bookCreated: " + createBook);


        assertNotNull(createBook);
        assertNotNull(createBook.getAuthor());
        assertNotNull(createBook.getLaunchDate());
        assertNotNull(createBook.getPrice());
        assertNotNull(createBook.getTitle());

        assertEquals("Zion", createBook.getAuthor());
        assertEquals(2.99, createBook.getPrice());
        assertEquals("Jah", createBook.getTitle());

    }

    @Test
    @Order(2)
    void testUpdateBook() throws IOException {
        mockBook();
        book.setTitle("Jah Update");
        book.setId(16L);
        var content =
                given().spec(specification)
                        .contentType(TestsConfigs.CONTENT_TYPE_JSON)
                        .body(book)
                        .when()
                        .put()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .asString();
        BookDataTest updatedBook = mapper.readValue(content, BookDataTest.class);
        book = updatedBook;

        System.out.println("book " + book);
        System.out.println("bookCreated: " + updatedBook);


        assertNotNull(updatedBook);
        assertNotNull(updatedBook.getAuthor());
        assertNotNull(updatedBook.getLaunchDate());
        assertNotNull(updatedBook.getPrice());
        assertNotNull(updatedBook.getTitle());

        assertEquals("Zion", updatedBook.getAuthor());
        assertEquals(book.getLaunchDate(), updatedBook.getLaunchDate());
        assertEquals(2.99, updatedBook.getPrice());
        assertEquals("Jah Update", updatedBook.getTitle());

    }

    @Test
    @Order(3)
    void testFindById() throws IOException {
        mockBook();
        book.setId(16l);

        var content =
                given().spec(specification)
                        .contentType(TestsConfigs.CONTENT_TYPE_JSON)
                        .header(TestsConfigs.HEADER_PARAM_ORIGIN, TestsConfigs.ORIGIN_8080)
                        .pathParam("id", book.getId())
                        .when()
                        .get("{id}")
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .asString();

        BookDataTest persistedBook = mapper.readValue(content, BookDataTest.class);

        assertNotNull(persistedBook);
        assertNotNull(persistedBook.getAuthor());
        assertNotNull(persistedBook.getLaunchDate());
        assertNotNull(persistedBook.getPrice());
        assertNotNull(persistedBook.getTitle());

        assertEquals("Zion", persistedBook.getAuthor());
        assertEquals(2.99, persistedBook.getPrice());
        assertEquals("Jah Update", persistedBook.getTitle());

    }

    @Test
    @Order(4)
    void testDelete() throws IOException {
        mockBook();
        book.setId(17l);

        var content =
                given().spec(specification)
                        .contentType(TestsConfigs.CONTENT_TYPE_JSON)
                        .header(TestsConfigs.HEADER_PARAM_ORIGIN, TestsConfigs.ORIGIN_8080)
                        .pathParam("id", book.getId())
                        .when()
                        .delete("{id}")
                        .then()
                        .statusCode(204);

    }

    @Test
    @Order(5)
    void testFindAll() throws IOException {
        var content =
                given().spec(specification)
                        .contentType(TestsConfigs.CONTENT_TYPE_JSON)
                        .when()
                        .get()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .asString();
        List<BookDataTest> books = mapper.readValue(content, new TypeReference<List<BookDataTest>>(){});
        BookDataTest foundBookOne = books.get(0);


        assertNotNull(foundBookOne.getId());
        assertNotNull(foundBookOne.getAuthor());
        assertNotNull(foundBookOne.getLaunchDate());
        assertNotNull(foundBookOne.getPrice());
        assertNotNull(foundBookOne.getTitle());

        assertTrue(foundBookOne.getId() > 0);

        assertEquals("Ralph Johnson, Erich Gamma, John Vlissides e Richard Helm", foundBookOne.getAuthor());
        assertEquals(45.0, foundBookOne.getPrice());
        assertEquals("Design Patterns", foundBookOne.getTitle());
    }

    @Test
    @Order(6)
    void testFindAllWithOutToken() throws IOException {

        RequestSpecification specWithOutToken = new RequestSpecBuilder()
                .setBasePath("/api/books")
                .setPort(TestsConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        given().spec(specWithOutToken)
                .contentType(TestsConfigs.CONTENT_TYPE_JSON)
                .when()
                .get()
                .then()
                .statusCode(403);

    }
    void mockBook(){
        book.setId(1l);
        book.setAuthor("Zion");
        book.setLaunchDate(Date.from(Instant.now()));
        book.setPrice(2.99);
        book.setTitle("Jah");

    }

}
