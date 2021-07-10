package br.com.restapi.suite;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import br.com.restapi.core.BaseTest;
import br.com.restapi.refatoracao.AuthTest;
import br.com.restapi.refatoracao.ContasTest;
import br.com.restapi.refatoracao.MovimentacaoTest;
import br.com.restapi.refatoracao.SaldoTest;
import io.restassured.RestAssured;

@RunWith(org.junit.runners.Suite.class)
@SuiteClasses({
	ContasTest.class,
	MovimentacaoTest.class,
	SaldoTest.class,
	AuthTest.class
})
public class Suite extends BaseTest{

	@BeforeClass
	public static void login() {
		// Extrair token

		Map<String, String> login = new HashMap<>();
		login.put("email", EMAIL);
		login.put("senha", SENHA);

		String TOKEN = given()
					.body(login)
				.when()
					.post("/signin")
				.then()
					.statusCode(200)
					.extract().path("token");
		
		RestAssured.requestSpecification.header("Authorization", "JWT " + TOKEN);
		
		RestAssured.get("/reset").then().statusCode(200);
	}
}