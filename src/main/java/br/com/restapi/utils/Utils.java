package br.com.restapi.utils;

import io.restassured.RestAssured;

public class Utils {

	public static Integer getIdContaPeloNome(String name){
		return RestAssured.get("/contas?nome="+name+"").then().extract().path("id[0]");
	}
	
	public static Integer getIdMovPeloNome(String mov){
		return RestAssured.get("/transacoes?descricao="+mov+"").then().extract().path("id[0]");
	}
}