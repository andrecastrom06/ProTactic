# language: pt

Funcionalidade: 1 - Cadastro de Atleta
  Como gestor de um clube
  Desejo cadastrar atletas seguindo as regras de transferências
  Para garantir que o elenco respeite as normas

Regra: O Contrato só pode ser cadastrado em período de transferência 
      (Junho até Agosto ou Dezembro até Fevereiro) 
      a menos que ele esteja sem contrato vigente, 
      com isso poderia cadastrar contrato a qualquer momento.


  Cenário: Contratar jogador COM contrato vigente fora da janela
    Dado que "Carlos Silva" com contrato ativo em outro clube existe
    E estamos em 15 de março (fora da janela de transferências)
    Quando eu tentar cadastrar esse atleta no meu clube
    Então não conseguirei realizar a contratação

  Cenário: Contratar jogador COM contrato vigente dentro da janela
    Dado que "João Pereira" com contrato ativo em outro clube existe
    E estamos em 10 de julho (dentro da janela de transferências)
    Quando eu tentar cadastrar esse atleta no meu clube
    Então o registro do atleta será adicionado à lista de atletas do clube

  Cenário: Contratar jogador SEM contrato fora da janela
    Dado que "Lucas Andrade" sem contrato existe
    E estamos em 20 de outubro (fora da janela de transferências)
    Quando eu tentar cadastrar esse atleta no meu clube
    Então o registro do atleta será adicionado à lista de atletas do clube

  Cenário: Contratar jogador SEM contrato dentro da janela
    Dado que "Rafael Souza" sem contrato existe
    E estamos em 5 de janeiro (dentro da janela de transferências)
    Quando eu tentar cadastrar esse atleta no meu clube
    Então o registro do atleta será adicionado à lista de atletas do clube
