package br.com.restapi.refatoracao;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import br.com.restapi.core.BaseTest;
import br.com.restapi.utils.Utils;

public class ContasTest extends BaseTest {
	
	@Test
	public void deveIncluirContaComSucesso() {
		given()
			.body("{\"nome\": \"Conta Inserida\"}")
		.when()
			.post("/contas")
		.then()
			.statusCode(201)
		;
	}
	
	@Test
	public void deveAlterarContaComSucesso() {
		Integer CONTA_ID = Utils.getIdContaPeloNome("Conta para alterar");
		
		given()
			.body("{\"nome\": \"Conta Alterada\"}")
			.pathParam("id", CONTA_ID)
		.when()
			.put("/contas/{id}")
		.then()
			.log().all()
			.statusCode(200)
			.body("nome", is("Conta Alterada"))
		;
	}
	
	@Test
	public void naoDeveIncluirContaComNomeRepetido() {
		given()
			.body("{\"nome\": \"Conta mesmo nome\"}")
		.when()
			.post("/contas")
		.then()
			.log().all()
			.statusCode(400)
			.body("error", is("Já existe uma conta com esse nome!"))
		;
	}
}