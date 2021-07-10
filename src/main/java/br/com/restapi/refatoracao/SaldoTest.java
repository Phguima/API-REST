package br.com.restapi.refatoracao;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import br.com.restapi.core.BaseTest;
import br.com.restapi.utils.Utils;

public class SaldoTest extends BaseTest {
	
	@Test
	public void deveCalcularSaldoDasContas() {
		Integer CONTA_ID = Utils.getIdContaPeloNome("Conta para saldo");
		
		given()
		.when()
			.get("/saldo")
		.then()
			.log().all()
			.statusCode(200)
			.body("find{it.conta_id == "+CONTA_ID+"}.saldo", is("534.00"))
		;
	}
}