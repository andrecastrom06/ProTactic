# language: pt

Funcionalidade: 11 - Criar premiação interna

  Regra: O atleta vencedor precisa estar com uma média acima de todos os outros
         e ter nota mínima de 6.
         Caso nenhum jogador tenha média >= 6, a premiação não será atribuída.

  Cenário: Nenhum jogador tem média >= 6
  Dado que os jogadores "Pedro" com média "4" e "André" com média "4" existem
  Quando eu criar a premiação do mês de "outubro"
  Então a premiação ficará sem vencedor

Cenário: Jogador com maior média e nota >= 6 vence a premiação
  Dado que os jogadores "Carlos" com média "7.5" e "João" com média "6.8" existem
  Quando eu criar a premiação do mês de "setembro"
  Então o jogador "Carlos" será definido como vencedor da premiação

Cenário: Dois jogadores acima de 6, mas apenas um com a maior média
  Dado que os jogadores "Marcos" com média "6.5" e "Felipe" com média "8.2" existem
  Quando eu criar a premiação do mês de "novembro"
  Então o jogador "Felipe" será definido como vencedor da premiação

