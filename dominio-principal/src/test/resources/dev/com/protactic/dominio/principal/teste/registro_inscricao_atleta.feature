# language: pt

Funcionalidade: 6 - Registro de inscrição de atletas em competições

Contexto:
  Dado que existe a competição "Copa Nordeste" identificada por "COMPETICAO-COPA-NE"
  E existe o elenco cadastrado com os jogadores:
    | nome  | idade | contrato_ativo |
    | João  | 17    | sim            |
    | Pedro | 15    | sim            |
    | Lucas | 18    | não            |

Cenário: Registrar inscrição válida
  Dado que João possui contrato ativo e tem 17 anos
  Quando o analista registrar a inscrição de João na competição "COMPETICAO-COPA-NE"
  Então João fica inscrito na competição "COMPETICAO-COPA-NE"
  E passa a estar elegível para jogos dessa competição

Cenário: Impedir inscrição de jogador menor de idade  
  Dado que Pedro possui contrato ativo e tem 15 anos
  Quando o analista tentar registrar a inscrição de Pedro na competição "COMPETICAO-COPA-NE"
  Então o sistema não permite o registro
  E o sistema exibe o erro "Jogador menor de 16 anos não pode ser inscrito"

Cenário: Impedir inscrição de jogador sem contrato ativo
  Dado que Lucas tem 18 anos mas não possui contrato ativo
  Quando o analista tentar registrar a inscrição de Lucas na competição "COMPETICAO-COPA-NE"
  Então o sistema não permite o registro
  E o sistema exibe o erro "Jogador sem contrato ativo não pode ser inscrito"