# language: pt
Funcionalidade: Abrir processos de dispensa/rescisão

  Regra: O atleta precisa estar com contrato no clube e não estar machucado.

  Cenário: Rescisão do contrato de um jogador saudável com contrato no clube
    Dado um jogador com contrato ativo e saudável
    Quando o analista solicitar a rescisão do seu contrato
    Então o contrato do jogador deve ser encerrado
    E o status do jogador deve ser "Passe Livre"

  Cenário: Tentativa de rescisão do contrato de um jogador machucado
    Dado um jogador com contrato ativo e machucado
    Quando o analista solicitar a rescisão do seu contrato
    Então o sistema deve bloquear a rescisão com a mensagem "Não é permitido dispensar jogadores que estão lesionados."
  
  Cenário: Tentativa de rescisão de um jogador saudável sem contrato no clube
    Dado um jogador sem contrato e saudável
    Quando o analista solicitar a rescisão do seu contrato
    Então o sistema deve bloquear a rescisão com a mensagem "Jogador não possui contrato para rescindir."