package br.com.jotave_erref.RestWithSpringBoot;

import br.com.jotave_erref.RestWithSpringBoot.configs.TestsConfigs;
import br.com.jotave_erref.RestWithSpringBoot.integrationTests.testcontainers.AbstractIntegrationsTests;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SwaggerIntegrationTest extends AbstractIntegrationsTests {

	@Test
	void shouldDisplaySwaggerUiPage() {

		var content =
				given()
						.basePath("/swagger-ui/index.html")
						.port(TestsConfigs.SERVER_PORT)
						.when()
						.get()
						.body()
						.asString();
		assertTrue(content.contains("Swagger UI"));
	}

}
