# Casos de Testes do projeto

Interface: https://seubarriga.wcaquino.me

API: https://barrigarest.wcaquino.me
#
Não deve acessar API sem token - GET/contas
- Deve retornar 401

Deve incluir conta com sucesso - POST/signin
- Recurso - https://barrigarest.wcaquino.me/contas
- Enviar e-mail e senha(Para logar)
- POST/contas - enviar apenas o nome da conta
- Extrair token para usar em outros cenários[IMPORTANTE]

Deve alterar conta com sucesso - PUT/contas/:id
- Enviar novo nome da conta

Não deve incluir conta com nome repetido - POST/contas
- Enviar apenas um nome de conta já existente na base

Deve inserir movimentação com sucesso - POST/transacoes
- Parâmetros:	
	- conta_id
	- usuario_id(Não obrigatório)
	- descricao(String qualquer)
	- envolvido(Nome do favorecido)
	- tipo(DESP/REC - Despesas/Receitas)
	- data_transacao(dd/mm/yyyy - String)
	- data_pagamento(dd/mm/yyyy - String)
	- valor(0.00f)
	- status(true/false)
	
Deve validar campos obrigatórios na movimentação - POST/transacoes

Não deve cadastrar movimentação futura - POST/transacoes
- Enviar data futura

Não deve excluir conta com movimentação - DELETE/contas/:id
- Conta com movimentação inserida

Deve calcular saldo das contas - GET/saldo
- Deve retornar somente contas que possuem movimentações

Deve remover movimentação - DELETE/transacoes/:id
- id da movimentação
