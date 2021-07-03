package br.com.restapi.core;

import io.restassured.http.ContentType;

public interface Constant {
	
	String APP_BASE_URL = "https://barrigarest.wcaquino.me";
	Integer APP_PORT = 443; //https -> 80
	String APP_BASE_PATH = "";
	
	ContentType APP_CONTENT_TYPE = ContentType.JSON;
	
	Long MAX_TIMEOUT = 5000L;
	
	String EMAIL = "pedro@outlook.com.br";
	String SENHA = "pedro123";
}