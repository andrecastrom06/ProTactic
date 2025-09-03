# language: pt
Funcionalidade: Registrar proposta de contratação de novos jogadores

  Regra: Não se pode registrar uma proposta para um atleta que tenha contrato ativo com o clube,
         nem pode fazer propostas para atletas que estão com clubes fora do período de
         transferência (dezembro à fevereiro ou junho à agosto).

  Cenário: Registro de um jogador sem contrato
    Dado um jogador chamado "Felipe" que não tem contrato
    E a data atual é "15/03/2025"
    Quando um analista criar uma proposta de contrato para "Felipe"
    Então a proposta deve ser registrada com sucesso

  Cenário: Tentativa de registro de um jogador com contrato ativo no mesmo clube
    Dado um jogador chamado "Braz" que tem contrato ativo com o "Santa Cruz Futebol Clube"
    E a data atual é "15/01/2025"
    Quando um analista tentar criar uma proposta de contrato para "Braz"
    Então o sistema deve lançar uma exceção com a mensagem "Jogador já possui contrato ativo com o clube."

  Cenário: Renovação de um jogador dentro da janela de transferências
    Dado um jogador chamado "Jotinha" que tem contrato ativo com o "Santa Cruz Futebol Clube"
    E a data atual é "15/01/2025"
    Quando um analista criar uma proposta de RENOVAÇÃO de contrato para "Jotinha"
    Então a proposta de renovação deve ser registrada com sucesso

  Cenário: Registro de um jogador com contrato em outro clube fora da janela de transferências
    Dado um jogador chamado "Wagner" que tem contrato ativo com o "Sport Club do Recife"
    E a data atual é "15/03/2025"
    Quando um analista tentar criar uma proposta de contrato para "Wagner"
    Então o sistema deve lançar uma exceção com a mensagem "Não é permitido fazer propostas fora da janela de transferência."