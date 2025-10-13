# language: pt

Funcionalidade: 9 - Atribuição de nota técnica e observações
  Como treinador
  Quero atribuir notas técnicas e observações aos jogadores
  Para avaliar o desempenho em cada jogo

  Regra: Somente quem atuou pode receber nota, e a nota deve ser de 0 a 10
    Apenas jogadores que participaram de um jogo podem receber nota técnica.
    A nota técnica deve estar entre 0 e 10 (inclusive).
    Observações do treinador podem ser registradas com ou sem nota.
    Observações sem nota não entram no cálculo de média de desempenho.

  Contexto:
    Dado que existe o jogo "Sport x Náutico" identificado por "JOGO-NAUTICO"
    E existe o elenco de avaliação cadastrado com os jogadores:
      | nome  | posição   |
      | João  | atacante  |
      | Pedro | lateral   |
      | Lucas | zagueiro  |
      | Caio  | meia      |

  Cenário: Atribuir nota e observação a jogador que atuou
    Dado que João participou do jogo "JOGO-NAUTICO"
    Quando o treinador registrar a nota 8,5 com a observação "boa movimentação e finalização" para João no jogo "JOGO-NAUTICO"
    Então a nota técnica 8,5 e a observação "boa movimentação e finalização" são vinculadas ao desempenho de João no jogo "JOGO-NAUTICO"
    E o registro de desempenho de João no jogo "JOGO-NAUTICO" passa a exibir "nota_tecnica=8,5" e "observacao=boa movimentação e finalização"

  Cenário: Impedir atribuição de nota a jogador que não atuou
    Dado que Pedro não participou do jogo "JOGO-NAUTICO"
    Quando o treinador tentar registrar a nota 7 para Pedro no jogo "JOGO-NAUTICO"
    Então o sistema de avaliação não permite o registro
    E o sistema de avaliação exibe o erro "Apenas jogadores que atuaram podem receber nota técnica"

  Cenário: Impedir nota fora do intervalo válido
    Dado que Lucas participou do jogo "JOGO-NAUTICO"
    Quando o treinador tentar registrar a nota 12 para Lucas no jogo "JOGO-NAUTICO"
    Então o sistema de avaliação não permite o registro
    E o sistema de avaliação exibe o erro "Nota inválida. Informe um valor entre 0 e 10"

  Cenário: Registrar observação sem nota
    Dado que Caio participou do jogo "JOGO-NAUTICO"
    Quando o treinador registrar apenas a observação "precisa melhorar o passe" para Caio no jogo "JOGO-NAUTICO"
    Então a observação "precisa melhorar o passe" é vinculada ao desempenho de Caio no jogo "JOGO-NAUTICO"
    E o registro de desempenho de Caio no jogo "JOGO-NAUTICO" não possui "nota_tecnica"
    E a observação não impacta o cálculo de média de desempenho do jogador