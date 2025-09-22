Feature: Registrar proposta de contratação de novos jogadores

  Scenario: "Kieza" não tem contrato e "Sport" tenta contratá-lo
    Given um jogador chamado "Kieza" que não tem contrato
    When um analista do "Sport" cria uma proposta de contrato para "Kieza"
    Then a proposta deve ser registrada com sucesso

  Scenario: "Kieza" já tem contrato com o "Sport" que tenta contratá-lo
    Given um jogador chamado "Kieza" que tem contrato ativo com o "Sport"
    When um analista do "Sport" cria uma proposta de contrato para "Kieza"
    Then o sistema deve lançar uma exceção com a mensagem "Jogador já possui contrato ativo com o clube."

  Scenario: "Kieza" tem contrato e "Sport" tenta contratá-lo em "25/01/2025"
    Given um jogador chamado "Kieza" que tem contrato
    And a data é "25/01/2025"
    When um analista do "Sport" cria uma proposta de contrato para "Kieza"
    Then a proposta deve ser registrada com sucesso

  Scenario: "Kieza" tem contrato e "Sport" tenta contratá-lo em "25/04/2025"
    Given um jogador chamado "Kieza" que tem contrato
    And a data é "25/04/2025"
    When um analista do "Sport" cria uma proposta de contrato para "Kieza"
    Then a proposta não deve ser registrada com sucesso