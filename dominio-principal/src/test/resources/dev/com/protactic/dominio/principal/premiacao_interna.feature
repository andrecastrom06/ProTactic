# language: pt


Funcionalidade: 11 - Criar premiações diferentes
  Como treinador, desejo criar premiações para os atletas do meu time com base em seu desempenho, para reconhecer os melhores.


Regra: O atleta vencedor precisa estar 
      com uma média acima de todos os outros em nota e com a nota mínima sendo 6,
      caso não tenha jogadores com nota média acima de 6 o prêmio fica sem um ganhador



  Cenário: Atribuir a premiação ao jogador com maior pontuação
    Dado que um jogador obteve a maior pontuação no mês de setembro, com nota média de 8,9
    Quando o treinador for criar o prêmio de melhor jogador do time
    Então o prêmio será atribuído a este jogador

  Cenário: Não atribuir a premiação a um jogador com pontuação menor que 6
    Dado que o jogador de maior pontuação no mês de outubro tem uma nota média de 5,5
    Quando o treinador for criar o prêmio de melhor jogador do time
    Então o prêmio não será atribuído a este jogador

  Cenário: Não atribuir a premiação ao segundo jogador com maior pontuação
    Dado que um jogador obteve a segunda maior pontuação no mês de setembro
    Quando o treinador for criar o prêmio de melhor jogador do time
    Então o prêmio não será atribuído a este jogador
