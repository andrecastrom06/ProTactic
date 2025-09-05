# language: pt

Funcionalidade: 7 - Definição de Capitão e Vice-Capitão

  Regra: Não se pode definir como capitão (ou vice-capitão) um jogador sem contrato ativo ou que não tenha minutagem constante e mais de 1 ano de clube.
         O capitão e o vice-capitão não podem ser o mesmo jogador.

  Contexto:
    Dado que o treinador está na tela de gerenciamento do time do PSG

  Cenário: Definir como capitão um jogador com todos os critérios válidos
    Dado que o jogador "Rodrigo" tem contrato ativo
    E "Rodrigo" tem 4 anos de clube
    E "Rodrigo" é um jogador consistente em minutos jogados
    Quando o treinador tentar definir "Rodrigo" como capitão
    Então "Rodrigo" será definido como capitão do PSG durante a temporada

  Cenário: Tentar definir como capitão um jogador com minutagem inconsistente
    Dado que o jogador "Vitinha" tem contrato ativo
    E "Vitinha" tem 4 anos de clube
    E "Vitinha" não tem minutagem constante
    Quando o treinador tentar definir "Vitinha" como capitão
    Então "Vitinha" não poderá ser definido como capitão do PSG durante a temporada

  Cenário: Tentar definir como capitão um jogador com menos de 1 ano de clube
    Dado que o jogador "Miguel" tem contrato ativo
    E "Miguel" tem 6 meses de clube
    E "Miguel" é um jogador consistente em minutos jogados
    Quando o treinador tentar definir "Miguel" como capitão
    Então "Miguel" não poderá ser definido como capitão do PSG durante a temporada

  Cenário: Tentar definir como capitão um jogador sem contrato ativo
    Dado que o jogador "Vinicius" não tem contrato ativo com o PSG
    E "Vinicius" tem 4 anos de clube
    E "Vinicius" é um jogador consistente em minutos jogados
    Quando o treinador tentar definir "Vinicius" como capitão
    Então "Vinicius" não poderá ser definido como capitão do PSG durante a temporada

  Esquema do Cenário: Tentar definir como capitão um jogador que não cumpre múltiplos critérios
    Dado que o jogador <nome> tem o status de contrato <status_contrato>
    E <nome> tem <tempo_clube> de clube
    E <nome> tem uma minutagem <minutagem>
    Quando o treinador tentar definir <nome> como capitão
    Então <nome> não poderá ser definido como capitão do PSG durante a temporada

    Exemplos:
      | nome         | status_contrato | tempo_clube | minutagem    |
      | "Thiagão"    | "ativo"         | "6 meses"   | "inconsistente" |
      | "João Lucas" | "inativo"       | "6 meses"   | "consistente"   |
      | "André"      | "inativo"       | "4 anos"    | "inconsistente" |
      | "Luiz Felipe"| "inativo"       | "6 meses"   | "inconsistente" |
