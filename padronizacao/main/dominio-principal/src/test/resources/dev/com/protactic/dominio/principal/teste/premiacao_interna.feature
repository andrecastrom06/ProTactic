# language: pt

Funcionalidade: 11 - Criar premiação interna

  Regra: 
    O atleta vencedor precisa estar com uma média acima de todos os outros
    E ter nota mínima de 6.
    Caso nenhum jogador tenha média >= 6, a premiação não será atribuída.
    Em caso de empate na maior média, vence o jogador com o menor desvio padrão.
    Caso a maior média seja exatamente 6.0, o jogador com nota 6 poderá ser vencedor.

  Cenário: Nenhum jogador tem média >= 6
    Dado que "Pedro" com média "5,9" no período de "outubro" existe
    E "André" com média "5,8" no período de "outubro" existe
    Quando eu criar a premiação do mês de "outubro"
    Então a premiação ficará sem vencedor

  Cenário: Apenas um jogador possui média >= 6
    Dado que "Carlos" com média "7.2" no período de "fevereiro" existe
    E "João" com média "5.5" no período de "fevereiro" existe
    Quando eu criar a premiação do mês de "fevereiro"
    Então o jogador "Carlos" será definido como vencedor da premiação

  Cenário: Dois jogadores acima de 6, mas apenas um com a maior média
    Dado que "Marcos" com média "6.5" no período de "novembro" existe
    E "Felipe" com média "8.2" no período de "novembro" existe
    Quando eu criar a premiação do mês de "novembro"
    Então o jogador "Felipe" será definido como vencedor da premiação

  Cenário: Empate de médias - menor desvio padrão vence
    Dado que "Rafael" com média "7.5" no período de "dezembro" existe
    E "Bruno" com média "7.5" no período de "dezembro" existe
    Quando eu criar a premiação do mês de "dezembro"
    Então o jogador com menor desvio padrão será definido como vencedor da premiação

  Cenário: Maior média exatamente 6.0
    Dado que "Lucas" com média "6.0" no período de "janeiro" existe
    E "Tiago" com média "5.9" no período de "janeiro" existe
    Quando eu criar a premiação do mês de "janeiro"
    Então o jogador "Lucas" será definido como vencedor da premiação
