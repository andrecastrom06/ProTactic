# language: pt

Funcionalidade: 4 - Definir esquema tático e escalação

  Regra: Para registrar uma escalação deve haver jogo no calendário. 
         O jogador precisa ter contrato ativo no clube, não estar lesionado (exceto grau 0) 
         e não estar suspenso.

  Contexto:
    Dado que o treinador está na tela de gerenciamento de escalação e tática

  Cenário: Registrar um esquema tático para o jogo do próximo sábado dia 23/08
    Dado que existe um jogo marcado para "23/08"
    Quando o treinador cadastrar a escalação
    Então a escalação aparecerá vinculada ao jogo do dia "23/08"

  Cenário: Registrar um esquema tático com jogador lesionado (grau 0)
    Dado que existe um jogo marcado para "24/08"
    E o jogador "Lucas" tem uma lesão de grau 0
    E "Lucas" possui contrato ativo e não está suspenso
    Quando o treinador cadastrar a escalação incluindo "Lucas"
    Então a escalação será registrada com sucesso

  Cenário: Registrar um esquema tático com jogador lesionado (grau 1)
    Dado que existe um jogo marcado para "24/08"
    E o jogador "Rafael" tem uma lesão de grau 1
    E "Rafael" possui contrato ativo e não está suspenso
    Quando o treinador cadastrar a escalação incluindo "Rafael"
    Então a escalação não poderá ser registrada

  Cenário: Registrar um esquema tático com jogador lesionado (grau 2)
    Dado que existe um jogo marcado para "24/08"
    E o jogador "Carlos" tem uma lesão de grau 2
    E "Carlos" possui contrato ativo e não está suspenso
    Quando o treinador cadastrar a escalação incluindo "Carlos"
    Então a escalação não poderá ser registrada

  Cenário: Registrar um esquema tático com jogador lesionado (grau 3)
    Dado que existe um jogo marcado para "24/08"
    E o jogador "Pedro" tem uma lesão de grau 3
    E "Pedro" possui contrato ativo e não está suspenso
    Quando o treinador cadastrar a escalação incluindo "Pedro"
    Então a escalação não poderá ser registrada

  Cenário: Registrar um esquema tático com jogador suspenso
    Dado que existe um jogo marcado para "24/08"
    E o jogador "João" está suspenso
    E "João" possui contrato ativo e não está lesionado
    Quando o treinador cadastrar a escalação incluindo "João"
    Então a escalação não poderá ser registrada

  Esquema do Cenário: Tentar registrar escalação inválida
    Dado que existe um jogo marcado para "24/08"
    E o jogador <nome> tem a condição <condicao>
    E <nome> possui contrato <status_contrato>
    Quando o treinador cadastrar a escalação incluindo <nome>
    Então a escalação não poderá ser registrada

    Exemplos:
      | nome       | condicao         | status_contrato |
      | "André"    | "lesão grau 1"   | ativo           |
      | "Felipe"   | "lesão grau 2"   | ativo           |
      | "Diego"    | "lesão grau 3"   | ativo           |
      | "Marcos"   | "suspenso"       | ativo           |
      | "Vinicius" | "saudável"       | inativo         |
