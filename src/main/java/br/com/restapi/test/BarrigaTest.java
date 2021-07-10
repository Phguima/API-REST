package br.com.restapi.test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import br.com.restapi.core.BaseTest;
import br.com.restapi.core.Movimentacao;
import br.com.restapi.utils.DateUtils;
import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BarrigaTest extends BaseTest {
	
	private static String CONTA_NAME = "Conta " + System.nanoTime();
	private static Integer CONTA_ID;
	private static Integer MOV_ID;
	
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
	}
	
	@Test
	public void ct01_deveIncluirContaComSucesso() {
		CONTA_ID = given()
			.body("{\"nome\": \""+CONTA_NAME+"\"}")
		.when()
			.post("/contas")
		.then()
			.statusCode(201)
			.extract().path("id")
		;
	}
	
	@Test
	public void ct02_deveAlterarContaComSucesso() {
		given()
			.body("{\"nome\": \""+CONTA_NAME+" Alterada\"}")
			.pathParam("id", CONTA_ID)
		.when()
			.put("/contas/{id}")
		.then()
			.log().all()
			.statusCode(200)
			.body("nome", is(""+CONTA_NAME+" Alterada"))
		;
	}
	
	@Test
	public void ct03_naoDeveIncluirContaComNomeRepetido() {
		given()
			.body("{\"nome\": \""+CONTA_NAME+" Alterada\"}")
		.when()
			.post("/contas")
		.then()
			.log().all()
			.statusCode(400)
			.body("error", is("Já existe uma conta com esse nome!"))
		;
	}
	
	@Test
	public void ct04_deveInserirMovimentacaoComSucesso() {
		Movimentacao mov = getMovimentacaoValida();

		MOV_ID = given()
			.body(mov)
		.when()
			.post("/transacoes")
		.then()
			.log().all()
			.statusCode(201)
			.extract().path("id")
		;
	}
	
	@Test
	public void ct05_deveValidarCamposObrigatóriosNaMovimentacao() {
		given()
			.body("{}")
		.when()
			.post("/transacoes")
		.then()
			.log().all()
			.statusCode(400)	
			.body("$", hasSize(8))
			.body("msg", hasItems("Data da Movimentação é obrigatório",
								"Data do pagamento é obrigatório",
								"Descrição é obrigatório",
								"Interessado é obrigatório",
								"Valor é obrigatório",
								"Valor deve ser um número",
								"Conta é obrigatório",
								"Situação é obrigatório"
					))
		;
	}
	
	@Test
	public void ct06_naoDeveCadastrarMovimentacaoFutura() {
		Movimentacao mov = getMovimentacaoValida();
		mov.setData_transacao(DateUtils.getData(2));
		
		given()
			.body(mov)
		.when()
			.post("/transacoes")
		.then()
			.log().all()
			.statusCode(400)
			.body("$", hasSize(1))
			.body("msg", hasItem("Data da Movimentação deve ser menor ou igual à data atual"))
		;
	}
	
	@Test
	public void ct07_naoDeveExcluirContaComMovimentacao() {
		given()
			.pathParam("id", CONTA_ID)
		.when()
			.delete("/contas/{id}")
		.then()
			.log().all()
			.statusCode(500)
			.body("constraint", is("transacoes_conta_id_foreign"))
		;
	}
	
	@Test
	public void ct08_deveCalcularSaldoDasContas() {
		given()
		.when()
			.get("/saldo")
		.then()
			.log().all()
			.statusCode(200)
			.body("find{it.conta_id == "+CONTA_ID+"}.saldo", is("100.00"))
		;
	}
	
	@Test
	public void ct09_deveRemoverMovimentacao() {
		given()
			.pathParam("id", MOV_ID)
		.when()
			.delete("/transacoes/{id}")
		.then()
			.log().all()
			.statusCode(204)
		;
	}
	
	@Test
	public void ct10_naoDeveAcessarAPISemToken() {
		FilterableRequestSpecification req = (FilterableRequestSpecification) RestAssured.requestSpecification;
		req.removeHeader("Authorization");
		
		given()
		.when()
			.get("/contas")
		.then()
			.statusCode(401)
		;
	}
	
	private Movimentacao getMovimentacaoValida() {
		Movimentacao mov = new Movimentacao();
		mov.setConta_id(CONTA_ID);
//		mov.setUsuario_id(APP_PORT);
		mov.setDescricao("Descrição");
		mov.setEnvolvido("Sr. Aparecido");
		mov.setTipo("REC");
		mov.setData_transacao(DateUtils.getData(-1));
		mov.setData_pagamento(DateUtils.getData(5));
		mov.setValor(100f);
		mov.setStatus(true);
		
		return mov;
	}
}