Feature: Abrir processos de dispensa/rescisão

  Scenario: "Kieza" está saudável e com contrato ativo no "Sport" mas o clube quer dispensá-lo
    Given um jogador chamado "Kieza" com contrato ativo com o "Sport" 
    And o jogador está saudável
    When o analista do "Sport" solicitar a rescisão do seu contrato
    Then o clube do jogador deve ser "Passes Livres"

  Scenario: "Kieza" está machucado e com contrato ativo no "Sport" mas o clube quer dispensá-lo
    Given um jogador chamado "Kieza" com contrato ativo com o "Sport" 
    And o jogador está machucado
    When o analista do "Sport" solicitar a rescisão do seu contrato
    Then o sistema deve bloquear a rescisão com a mensagem "Não é permitido dispensar jogadores que estão lesionados."