# language: pt
Funcionalidade: API - Produtos

  Cenário: Listar produtos cadastrados na plataforma
    Quando Solicitar a lista de produtos cadastrados na plataforma
    Então a lista de produtos é retornada com sucesso
    
  Cenário: Listar produtos por código de categoria
  	Dado um código de categoria para consulta
    Quando solicitar a lista de produtos da categoria
    Então a lista de produtos é retornada com sucesso
    
  Cenário: Listar produtos por nome de categoria
  	Dado um nome de categoria para consulta
    Quando solicitar a lista de produtos da categoria por nome
    Então a lista de produtos por nome de categoria é retornada com sucesso
    
  Cenário: Adicionar produtos por categoria
    Dado um código de categoria
    Quando adicionar produtos na categoria selecionada
    Então novo produto deve ser adicionado com sucesso
    
  Cenário: Remover produto da plataforma
    Dado que um produto esteja cadastrado na plataforma
    Quando for solicitado a remoção do produto
    Então o produto deve ser removido da plataforma
    
	Cenário: Atualizar produto na plataforma
		Dado que um produto esteja cadastrado na plataforma para alteração
    Quando atualizar o produto na plataforma
    Então produto deve ser atualizado com sucesso