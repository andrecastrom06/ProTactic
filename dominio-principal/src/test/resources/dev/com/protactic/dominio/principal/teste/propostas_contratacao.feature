# language: pt

Funcionalidade: 5 - Registrar proposta de contratação de novos jogadores

Regra: Não se pode registrar uma proposta para um atleta que tenha contrato ativo com o clube, 
      nem pode fazer propostas para atletas que estão com clubes fora do período de transferência 
      (dezembro à fevereiro ou junho à agosto)

  Cenário: "Kieza" não tem contrato e "Sport" tenta contratá-lo
    Dado um jogador chamado "Kieza" que não tem contrato
    E a data é "25/01/2025"
    Quando um analista do "Sport" cria uma proposta de contrato para "Kieza"
    Então a proposta deve ser registrada com sucesso

  Cenário: "Kieza" já tem contrato com o "Sport" que tenta contratá-lo
    Dado um jogador chamado "Kieza" que tem contrato com o "Sport"
    Quando um analista do "Sport" cria uma proposta de contrato para "Kieza"
    Então o sistema deve lançar uma exceção com a mensagem "Jogador já possui contrato ativo com o clube."

  Cenário: "Kieza" tem contrato com o "Santos" e o "Sport" tenta contratá-lo em "25/01/2025"
    Dado um jogador chamado "Kieza" que tem contrato com o "Santos"
    E a data é "25/01/2025"
    Quando um analista do "Sport" cria uma proposta de contrato para "Kieza"
    Então a proposta deve ser registrada com sucesso

  Cenário: "Kieza" tem contrato com o "Santos" e o "Sport" tenta contratá-lo em "25/04/2025"
    Dado um jogador chamado "Kieza" que tem contrato com o "Santos"
    E a data é "25/04/2025"
    Quando um analista do "Sport" cria uma proposta de contrato para "Kieza"
    Então o sistema deve lançar uma exceção com a mensagem "Jogador não pode ser contratado fora do prazo de transferência."