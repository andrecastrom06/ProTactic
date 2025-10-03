# language: pt

Funcionalidade: Cadastro de Atleta
  Como gestor de um clube
  Desejo cadastrar atletas seguindo as regras de transferências
  Para garantir que o elenco respeite as normas

  Contexto:
    Dado que o ano atual é 2025

  Cenário: Contratar jogador COM contrato vigente fora da janela
    Dado que um "lateral direito" com contrato ativo em outro clube existe
    E estamos em 15 de março (fora da janela de transferências)
    Quando eu tentar cadastrar esse atleta no meu clube
    Então não conseguirei realizar a contratação

  Cenário: Contratar jogador COM contrato vigente dentro da janela
    Dado que um "meia" com contrato ativo em outro clube existe
    E estamos em 10 de julho (dentro da janela de transferências)
    Quando eu tentar cadastrar esse atleta no meu clube
    Então o registro do atleta será adicionado à lista de atletas do clube

  Cenário: Contratar jogador SEM contrato fora da janela
    Dado que um "zagueiro" sem contrato existe
    E estamos em 20 de outubro (fora da janela de transferências)
    Quando eu tentar cadastrar esse atleta no meu clube
    Então o registro do atleta será adicionado à lista de atletas do clube

  Cenário: Contratar jogador SEM contrato dentro da janela
    Dado que um "goleiro" sem contrato existe
    E estamos em 5 de janeiro (dentro da janela de transferências)
    Quando eu tentar cadastrar esse atleta no meu clube
    Então o registro do atleta será adicionado à lista de atletas do clube
