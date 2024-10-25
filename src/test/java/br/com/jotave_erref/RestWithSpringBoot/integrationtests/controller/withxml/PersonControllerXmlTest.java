package br.com.jotave_erref.RestWithSpringBoot.integrationtests.controller.withxml;

import br.com.jotave_erref.RestWithSpringBoot.configs.TestsConfigs;
import br.com.jotave_erref.RestWithSpringBoot.integrationtests.data.PersonDataTest;
import br.com.jotave_erref.RestWithSpringBoot.integrationtests.data.TokenDataTest;
import br.com.jotave_erref.RestWithSpringBoot.integrationtests.data.UserData;
import br.com.jotave_erref.RestWithSpringBoot.integrationtests.testcontainers.AbstractIntegrationsTests;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8888"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerXmlTest extends AbstractIntegrationsTests {

	private static RequestSpecification specification;
	private static XmlMapper mapper;

	private static PersonDataTest person;

	@BeforeAll
	public static void setUp(){
		mapper = new XmlMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); //how XML deserialization will work. Disable failures when they are unknown. In this scenario it will ignore HATEOAS Links
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
						.contentType(TestsConfigs.CONTENT_TYPE_XML)
						.accept(TestsConfigs.CONTENT_TYPE_XML)
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
						.contentType(TestsConfigs.CONTENT_TYPE_XML)
						.accept(TestsConfigs.CONTENT_TYPE_XML)
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

		System.out.println("person: " + person);
		System.out.println("personCreated: " + createdPerson);


		assertNotNull(createdPerson);
		assertNotNull(createdPerson.getFirstName());
		assertNotNull(createdPerson.getLastName());
		assertNotNull(createdPerson.getAddress());
		assertNotNull(createdPerson.getGender());

		assertEquals("Zion", createdPerson.getFirstName());
		assertEquals("Selassie", createdPerson.getLastName());
		assertEquals("Rua xxx", createdPerson.getAddress());
		assertEquals("Male", createdPerson.getGender());

	}

	@Test
	@Order(2)
	void testUpdate() throws IOException {
		person.setAddress("Rua yyy");
		person.setId(2l);

		var content =
				given().spec(specification)
						.contentType(TestsConfigs.CONTENT_TYPE_XML)
						.accept(TestsConfigs.CONTENT_TYPE_XML)
						.body(person)
						.when()
						.put()
						.then()
						.statusCode(200)
						.extract()
						.body()
						.asString();

		PersonDataTest createdPerson = mapper.readValue(content, PersonDataTest.class);
		person = createdPerson;

		assertNotNull(createdPerson);
		assertNotNull(createdPerson.getFirstName());
		assertNotNull(createdPerson.getLastName());
		assertNotNull(createdPerson.getAddress());
		assertNotNull(createdPerson.getGender());

		assertTrue(createdPerson.getEnabled());

		assertEquals(person.getFirstName(), createdPerson.getFirstName());

		assertEquals("Zion", createdPerson.getFirstName());
		assertEquals("Selassie", createdPerson.getLastName());
		assertEquals("Rua yyy", createdPerson.getAddress());
		assertEquals("Male", createdPerson.getGender());

	}

	@Test
	@Order(3)
	void testDisablePersonById() throws IOException {
		person.setId(2L);

		var content =
				given().spec(specification)
						.contentType(TestsConfigs.CONTENT_TYPE_XML)
						.accept(TestsConfigs.CONTENT_TYPE_XML)
						.header(TestsConfigs.HEADER_PARAM_ORIGIN, TestsConfigs.ORIGIN_8080)
						.pathParam("id", person.getId())
						.when()
						.patch("{id}")
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

		assertFalse(persistedPerson.getEnabled());

		assertFalse(persistedPerson.getEnabled());

		assertEquals("Zion", persistedPerson.getFirstName());
		assertEquals("Selassie", persistedPerson.getLastName());
		assertEquals("Rua yyy", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());

	}

	@Test
	@Order(4)
	void testFindById() throws IOException {
		mockPerson();
		person.setId(2l);

		var content =
				given().spec(specification)
						.contentType(TestsConfigs.CONTENT_TYPE_XML)
						.accept(TestsConfigs.CONTENT_TYPE_XML)
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

		assertFalse(persistedPerson.getEnabled());

		assertEquals("Zion", persistedPerson.getFirstName());
		assertEquals("Selassie", persistedPerson.getLastName());
		assertEquals("Rua yyy", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());

	}

	@Test
	@Order(5)
	void testDelete() throws IOException {
		mockPerson();
		person.setId(2l);

		var content =
				given().spec(specification)
						.contentType(TestsConfigs.CONTENT_TYPE_XML)
						.accept(TestsConfigs.CONTENT_TYPE_XML)
						.header(TestsConfigs.HEADER_PARAM_ORIGIN, TestsConfigs.ORIGIN_8080)
						.pathParam("id", person.getId())
						.when()
						.delete("{id}")
						.then()
						.statusCode(204);

	}

	@Test
	@Order(6)
	void testFindAll() throws IOException {

		var content =
				given().spec(specification)
						.contentType(TestsConfigs.CONTENT_TYPE_XML)
						.accept(TestsConfigs.CONTENT_TYPE_XML)
						.when()
						.get()
						.then()
						.statusCode(200)
						.extract()
						.body()
						.asString();
		List<PersonDataTest> people = mapper.readValue(content, new TypeReference<List<PersonDataTest>>(){});
		PersonDataTest foundPersonOne = people.get(0);


		assertNotNull(foundPersonOne.getId());
		assertNotNull(foundPersonOne.getFirstName());
		assertNotNull(foundPersonOne.getLastName());
		assertNotNull(foundPersonOne.getAddress());
		assertNotNull(foundPersonOne.getGender());

		assertEquals(1, foundPersonOne.getId());

		assertEquals("person1", foundPersonOne.getFirstName());
		assertEquals("person1", foundPersonOne.getLastName());
		assertEquals("address person id 1", foundPersonOne.getAddress());
		assertEquals("female", foundPersonOne.getGender());

	}

	@Test
	@Order(7)
	void testFindAllWithOutToken() throws IOException {

		RequestSpecification specWithOutToken = new RequestSpecBuilder()
				.setBasePath("/api/person")
				.setPort(TestsConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();

		given().spec(specWithOutToken)
						.contentType(TestsConfigs.CONTENT_TYPE_XML)
						.accept(TestsConfigs.CONTENT_TYPE_XML)
					 	.when()
						.get()
						.then()
						.statusCode(403);

	}

	private void mockPerson() {
		person.setFirstName("Zion");
		person.setLastName("Selassie");
		person.setAddress("Rua xxx");
		person.setGender("Male");
		person.setEnabled(true);
	}

}
