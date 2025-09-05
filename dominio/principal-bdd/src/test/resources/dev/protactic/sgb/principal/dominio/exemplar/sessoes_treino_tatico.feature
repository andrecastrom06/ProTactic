# language: pt

Funcionalidade: 10 - Criar sessões de treinos táticos vinculados a um jogo

  Regra: Deve haver um jogo no calendário para vincular um treino tático.
         Os atletas selecionados não podem estar indisponíveis (lesão grau 1, 2 ou 3, ou suspensão).
         Lesão grau 0 não impede a participação.

  Contexto:
    Dado que o treinador está na área de planejamento de treinos

  Cenário: Criar treino tático com jogo existente e atletas disponíveis
    Dado que existe o jogo "PSG vs Lyon" cadastrado no calendário
    E os jogadores "Neymar" e "Marquinhos" estão disponíveis (sem lesão ou com lesão grau 0)
    Quando o treinador criar uma sessão de treino tático vinculada ao jogo "PSG vs Lyon"
    Então o sistema deve permitir a criação da sessão
    E os jogadores "Neymar" e "Marquinhos" devem ser listados para seleção

  Cenário: Criar mais de uma sessão de treino para o mesmo jogo
    Dado que existe o jogo "PSG vs Marseille" no calendário
    E já existe uma "Sessão de Treino 1" vinculada a este jogo
    Quando o treinador criar uma nova "Sessão de Treino 2" para o mesmo jogo "PSG vs Marseille"
    Então o sistema deve permitir a criação
    E ambas as sessões, "Sessão de Treino 1" e "Sessão de Treino 2", devem estar vinculadas ao jogo "PSG vs Marseille"

  Cenário: Tentar criar treino tático sem jogo cadastrado no calendário
    Dado que não existe nenhum jogo cadastrado no calendário
    Quando o treinador tentar criar uma sessão de treino tático
    Então o sistema deve impedir a criação
    E exibir a mensagem: "Não há jogo no calendário para vincular este treino."

  Cenário: Atletas indisponíveis não devem ser listados para o treino
    Dado que existe o jogo "PSG vs Monaco" cadastrado no calendário
    E o jogador "Mbappé" está com lesão grau 2
    E o jogador "Verratti" está suspenso
    E o jogador "Hakimi" está disponível
    Quando o treinador criar uma sessão de treino tático para o jogo "PSG vs Monaco"
    Então os atletas "Mbappé" e "Verratti" não devem aparecer na lista de convocação do treino
    E o atleta "Hakimi" deve aparecer na lista de convocação
