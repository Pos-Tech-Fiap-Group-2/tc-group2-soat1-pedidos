# language: pt
Funcionalidade: API - Pedidos

  Cenário: Listar pedidos na plataforma
    Quando solicitar a lista de pedidos cadastrados na plataforma
    Então a lista de pedidos deve ser apresentada
    
  Cenário: Listar pedidos na plataforma por status
    Quando solicitar a lista de pedidos cadastrados na plataforma por status
    Então a lista de pedidos deve ser apresentada pelo status informado
    
  Cenário: Consultar pedido por id
    Quando consultar o pedido pelo id
    Então dados do pedido devem ser apresentados
    
  Cenário: Atualizar status do pedido
    Quando atualizar status do pedido na plataforma
    Então pedido com o status atualizado
    
  Cenário: Adicionar novos itens ao pedido
    Quando adicionar itens ao pedido
    Então itens adicionados com sucesso ao pedido
    
  Cenário: Atualizar itens ao pedido
    Quando atualizar itens ao pedido
    Então itens atualizados com sucesso ao pedido
    
  Cenário: Remover item do pedido
    Quando remover item do pedido
    Então item removido com sucesso