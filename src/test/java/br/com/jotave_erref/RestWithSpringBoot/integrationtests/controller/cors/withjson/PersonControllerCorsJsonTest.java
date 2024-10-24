package br.com.jotave_erref.RestWithSpringBoot.integrationtests.controller.cors.withjson;

import br.com.jotave_erref.RestWithSpringBoot.configs.TestsConfigs;
import br.com.jotave_erref.RestWithSpringBoot.integrationtests.data.PersonDataTest;
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
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8888"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerCorsJsonTest extends AbstractIntegrationsTests {

	private static RequestSpecification specification;
	private static ObjectMapper mapper;

	private static PersonDataTest person;

	@BeforeAll
	public static void setUp(){
		mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); //how JSON deserialization will work. Disable failures when they are unknown. In this scenario it will ignore HATEOAS Links

		person = new PersonDataTest();
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
				.setBasePath("/api/person")
				.setPort(TestsConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}

	@Test
	@Order(1)
	void testCreate() throws IOException {
		mockPerson();

		var content =
				given().spec(specification)
						.contentType(TestsConfigs.CONTENT_TYPE_JSON)
						.header(TestsConfigs.HEADER_PARAM_ORIGIN, TestsConfigs.ORIGIN_8080)
						.body(person)
						.when()
						.post()
						.then()
						.statusCode(200)
						.extract()
						.body()
						.asString();
		PersonDataTest createdPerson = mapper.readValue(content, PersonDataTest.class);
		person = createdPerson;
		System.out.println("persisted: " + createdPerson);


		assertNotNull(createdPerson);
		assertNotNull(createdPerson.getFirstName());
		assertNotNull(createdPerson.getLastName());
		assertNotNull(createdPerson.getAddress());
		assertNotNull(createdPerson.getGender());

		assertEquals("Jean", createdPerson.getFirstName());
		assertEquals("Victor", createdPerson.getLastName());
		assertEquals("Rua xxx", createdPerson.getAddress());
		assertEquals("Male", createdPerson.getGender());

	}

	@Test
	@Order(2)
	void testCreateWithWrongOrigin() throws IOException {
		mockPerson();

		var content =
				given().spec(specification)
						.contentType(TestsConfigs.CONTENT_TYPE_JSON)
						.header(TestsConfigs.HEADER_PARAM_ORIGIN, TestsConfigs.WRONG_ORIGIN_5000)
						.body(person)
						.when()
						.post()
						.then()
						.statusCode(403)
						.extract()
						.body()
						.asString();

		assertNotNull(content);

		assertEquals("Invalid CORS request", content);
	}

	@Test
	@Order(3)
	void testFindById() throws IOException {
		mockPerson();

		var content =
				given().spec(specification)
						.contentType(TestsConfigs.CONTENT_TYPE_JSON)
						.header(TestsConfigs.HEADER_PARAM_ORIGIN, TestsConfigs.ORIGIN_8080)
						.pathParam("id", person.getId())
						.when()
						.get("{id}")
						.then()
						.statusCode(200)
						.extract()
						.body()
						.asString();
		PersonDataTest persistedPerson = mapper.readValue(content, PersonDataTest.class);
		person = persistedPerson;

		assertNotNull(persistedPerson);
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());

		assertEquals("Jean", persistedPerson.getFirstName());
		assertEquals("Victor", persistedPerson.getLastName());
		assertEquals("Rua xxx", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());

	}

	@Test
	@Order(4)
	void testFindByIdWithWrongOrigin() throws IOException {
		mockPerson();

		var content =
				given().spec(specification)
						.contentType(TestsConfigs.CONTENT_TYPE_JSON)
						.header(TestsConfigs.HEADER_PARAM_ORIGIN, TestsConfigs.WRONG_ORIGIN_5000)
						.pathParam("id", person.getId())
						.when()
						.get("{id}")
						.then()
						.statusCode(403)
						.extract()
						.body()
						.asString();

		assertNotNull(content);

		assertEquals("Invalid CORS request", content);

	}

	private void mockPerson() {
		person.setId(2L);
		person.setFirstName("Jean");
		person.setLastName("Victor");
		person.setAddress("Rua xxx");
		person.setGender("Male");
	}

}
