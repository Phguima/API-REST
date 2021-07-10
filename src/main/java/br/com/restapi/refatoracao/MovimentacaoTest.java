package br.com.restapi.refatoracao;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import br.com.restapi.core.BaseTest;
import br.com.restapi.core.Movimentacao;
import br.com.restapi.utils.DateUtils;
import br.com.restapi.utils.Utils;

public class MovimentacaoTest extends BaseTest {
	
	@Test
	public void deveInserirMovimentacaoComSucesso() {
		Movimentacao mov = getMovimentacaoValida();

		given()
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
	public void naoDeveCadastrarMovimentacaoFutura() {
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
	public void naoDeveExcluirContaComMovimentacao() {
		Integer CONTA_ID = Utils.getIdContaPeloNome("Conta com movimentacao");
		
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
	public void deveRemoverMovimentacao() {
		Integer MOV_ID = Utils.getIdMovPeloNome("Movimentacao para exclusao");
		
		given()
			.pathParam("id", MOV_ID)
		.when()
			.delete("/transacoes/{id}")
		.then()
			.log().all()
			.statusCode(204)
		;
	}
	
	private Movimentacao getMovimentacaoValida() {
		Movimentacao mov = new Movimentacao();
		mov.setConta_id(Utils.getIdContaPeloNome("Conta para movimentacoes"));
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