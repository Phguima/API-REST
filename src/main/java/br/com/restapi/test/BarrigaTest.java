package br.com.restapi.test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import br.com.restapi.core.BaseTest;
import br.com.restapi.core.Movimentacao;

public class BarrigaTest extends BaseTest {
	
	private String TOKEN;
	
	@Before
	public void login() {
		// Extrair token

		Map<String, String> login = new HashMap<>();
		login.put("email", EMAIL);
		login.put("senha", SENHA);

		TOKEN = given()
					.body(login)
				.when()
					.post("/signin")
				.then()
					.statusCode(200)
					.extract().path("token");
	}

	@Test
	public void naoDeveAcessarAPISemToken() {
		given()
		.when()
			.get("/contas")
		.then()
			.statusCode(401)
		;
	}
	
	@Test
	public void deveIncluirContaComSucesso() {
		given()
			.header("Authorization", "JWT " + TOKEN) //JWT para API antiga e Bearer para API novas	
			.body("{\"nome\": \"Conta Tester\"}")
		.when()
			.post("/contas")
		.then()
			.statusCode(201)
		;
	}
	
	@Test
	public void deveAlterarContaComSucesso() {
		given()
			.header("Authorization", "JWT " + TOKEN)
			.body("{\"nome\": \"Conta Alterada\"}")
		.when()
			.put("/contas/666032")
		.then()
			.log().all()
			.statusCode(200)
			.body("nome", is("Conta Alterada"))
		;
	}
	
	@Test
	public void naoDeveIncluirContaComNomeRepetido() {
		given()
			.header("Authorization", "JWT " + TOKEN)
			.body("{\"nome\": \"Conta Alterada\"}")
		.when()
			.post("/contas")
		.then()
			.log().all()
			.statusCode(400)
			.body("error", is("Já existe uma conta com esse nome!"))
		;
	}
	
	@Test
	public void deveInserirMovimentacaoComSucesso() {
		Movimentacao mov = new Movimentacao();
		mov.setConta_id(666032);
//		mov.setUsuario_id(APP_PORT);
		mov.setDescricao("Descrição");
		mov.setEnvolvido("Sr. Aparecido");
		mov.setTipo("REC");
		mov.setData_transacao("01/01/2020");
		mov.setData_pagamento("01/05/2020");
		mov.setValor(100f);
		mov.setStatus(true);
		
		given()
			.header("Authorization", "JWT " + TOKEN)
			.body(mov)
		.when()
			.post("/transacoes")
		.then()
			.log().all()
			.statusCode(201)
		;
	}
	
	@Test
	public void deveValidarCamposObrigatóriosNaMovimentacao() {
		Movimentacao mov = new Movimentacao();
		mov.setConta_id(666032);
//		mov.setUsuario_id(APP_PORT);
		mov.setDescricao("Descrição");
		mov.setEnvolvido("Sr. Aparecido");
		mov.setTipo("REC");
		mov.setData_transacao("01/01/2020");
		mov.setData_pagamento("01/05/2020");
		mov.setValor(100f);
		mov.setStatus(true);
		
		given()
			.header("Authorization", "JWT " + TOKEN)
			.body(mov)
		.when()
			.post("/transacoes")
		.then()
			.log().all()
			.statusCode(201)
		;
	}
}