
Funcionalidade: Cadastro de Atleta
  Como gestor de um clube, desejo cadastrar atletas no meu time, seguindo as regras de período de transferências, para manter o elenco atualizado.

  Contexto:
    Dado que o ano atual é 2025

  Cenário: Contratar um jogador fora da janela de transferências
    Dado que um lateral direito com contrato em outro clube existe 
    E está dentro da janela de transferência
    Quando eu tentar cadastrar esse atleta no meu clube
    Então não conseguirei realizar a contratação

  Cenário: Contratar um jogador sem contrato dentro da janela de transferências
    Dado que um zagueiro sem contrato existe no dia 30 de outubro
    Quando eu tentar cadastrar esse atleta no meu clube
    Então o registro do atleta será adicionado à lista de atletas do clube

