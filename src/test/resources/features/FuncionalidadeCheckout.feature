# language: pt
Funcionalidade: API - Checkout

  Cenário: Efetuar checkout de pedido
  	Dado um produto e quantidade de itens e id do cliente
    Quando efetuar o checkout do pedido na plataforma
    Então checkout realizado com sucesso