# language: pt

Funcionalidade: Criar sessões de treinos táticos

  Regra: Criação de treinos táticos, voltados exclusivamente para jogos específicos e participarão apenas os atletas disponíveis para a partida.

  Cenário: Criar treino tático com jogo existente e atletas disponíveis
    Dado que existe o jogo "PSG vs Lyon" no calendário
    E que o jogador "Neymar" tem o status "disponível"
    E que o jogador "Marquinhos" tem o status "disponível"
    Quando o treinador cria uma sessão de treino para o jogo "PSG vs Lyon"
    Então o sistema deve permitir a criação da sessão
    E o jogador "Neymar" deve aparecer na lista de convocação
    E o jogador "Marquinhos" deve aparecer na lista de convocação

  Cenário: Criar mais de uma sessão de treino para o mesmo jogo
    Dado que existe o jogo "PSG vs Marseille" no calendário
    E que já existe a sessão de treino "Sessão de Treino 1" para o jogo "PSG vs Marseille"
    Quando o treinador cria a sessão de treino "Sessão de Treino 2" para o jogo "PSG vs Marseille"
    Então o sistema deve permitir a criação da sessão
    E a sessão de treino "Sessão de Treino 1" deve estar vinculada ao jogo "PSG vs Marseille"
    E a sessão de treino "Sessão de Treino 2" deve estar vinculada ao jogo "PSG vs Marseille"

  Cenário: Tentar criar treino tático sem jogo cadastrado no calendário
    Dado que não existem jogos no calendário
    Quando o treinador tenta criar uma sessão de treino tático
    Então o sistema deve impedir a criação da sessão
    E o sistema deve exibir a mensagem "Não há jogo no calendário para vincular este treino."

  Cenário: Atletas indisponíveis não devem ser listados para o treino
    Dado que existe o jogo "PSG vs Monaco" no calendário
    E que o jogador "Mbappé" tem o status "lesão grau 2"
    E que o jogador "Verratti" tem o status "suspenso"
    E que o jogador "Hakimi" tem o status "disponível"
    Quando o treinador cria uma sessão de treino para o jogo "PSG vs Monaco"
    Então o jogador "Mbappé" não deve aparecer na lista de convocação
    E o jogador "Verratti" não deve aparecer na lista de convocação
    E o jogador "Hakimi" deve aparecer na lista de convocação