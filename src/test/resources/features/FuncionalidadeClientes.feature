# language: pt
Funcionalidade: API - Clientes

  Cenário: Cadastrar cliente na plataforma
    Quando Solicitar a gravação dos dados do cliente
    Então o cliente é cadastrado com sucesso na plataforma.

  Cenário: Atualizar cliente já cadastrado na plataforma
    Dado que um cliente já existente na plataforma
    Quando requisitar atualização dos dados do cliente
    Então cliente é atualizado com sucesso

  Cenário: Consultar cliente cadastrado na plataforma
    Quando solicitar os dados de um cliente
    Então os dados devem ser apresentados com sucesso

  Cenário: Remover cliente cadastrado na plataforma
    Dado que um cliente esteja cadastrado na plataforma
    Quando for solicitado a remoção do cliente
    Então o cliente deve ser removido da plataforma
