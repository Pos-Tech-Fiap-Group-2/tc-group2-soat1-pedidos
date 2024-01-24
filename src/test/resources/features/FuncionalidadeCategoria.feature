# language: pt
Funcionalidade: API - Categorias

  Cenário: Listar todas as categorias
    Quando Solicitar todas as categorias
    Então a lista de categorias é retornada com sucesso.