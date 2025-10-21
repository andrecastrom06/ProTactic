# language: pt

Funcionalidade: 7 - Definição de Capitão

  Regra:
  Para ser capitão, o jogador precisa atender a todos os requisitos necessários, que são: mínimo de 1 ano de clube, minutagem "constante" e contrato ativo. 
  O critério de desempate entre dois ou mais possíveis capitães será o tempo de clube, no qual o com mais tempo de clube será o escolhido. 
  Caso haja empate em todos os requisitos entre dois ou mais possíveis capitães, o treinador deverá escolher o capitão manualmente.

  Contexto: Estamos no dia "19/10/2025"

  Cenário: "Rodrigo" cumpre todos os requisitos para ser capitão do "PSG"
    Dado um jogador chamado "Rodrigo"
    E ele possui contrato "ativo" com o "PSG"
    E ele chegou no dia "18/10/2024" no clube
    E sua minutagem é "constante"
    Quando o treinador tenta definir "Rodrigo" como capitão
    Então "Rodrigo" deve ser definido como capitão do "PSG"

  Cenário: "Vitinha" não tem minutagem constante para ser capitão do "PSG"
    Dado um jogador chamado "Vitinha"
    E ele possui contrato "ativo" com o "PSG"
    E ele chegou no dia "19/10/2021" no clube
    E sua minutagem é "inconstante"
    Quando o treinador tenta definir "Vitinha" como capitão
    Então "Vitinha" não deve ser definido como capitão do "PSG"

  Cenário: "Miguel" tem menos de 1 ano de clube para ser capitão do "PSG"
    Dado um jogador chamado "Miguel"
    E ele possui contrato "ativo" com o "PSG"
    E ele chegou no dia "19/04/2025" no clube
    E sua minutagem é "constante"
    Quando o treinador tenta definir "Miguel" como capitão
    Então "Miguel" não deve ser definido como capitão do "PSG"

  Cenário: "Vinicius" não tem contrato ativo para ser capitão do "PSG"
    Dado um jogador chamado "Vinicius"
    E ele possui contrato "inativo" com o "PSG"
    E ele chegou no dia "19/10/2021" no clube
    E sua minutagem é "constante"
    Quando o treinador tenta definir "Vinicius" como capitão
    Então "Vinicius" não deve ser definido como capitão do "PSG"

  Cenário: "Luiz Felipe" não cumpre nenhum dos critérios para ser capitão
    Dado um jogador chamado "Luiz Felipe"
    E ele possui contrato "inativo" com o "PSG"
    E ele chegou no dia "19/04/2025" no clube
    E sua minutagem é "inconstante"
    Quando o treinador tenta definir "Luiz Felipe" como capitão
    Então "Luiz Felipe" não deve ser definido como capitão do "PSG"

  Cenário: Desempate pelo tempo de clube
    Dado dois jogadores "Rodrigo" e "Vitinha"
    E ambos possuem contrato "ativo" com o "PSG"
    E ambos têm minutagem "constante"
    E "Rodrigo" chegou no dia "19/04/2022" e "Vitinha" chegou no dia "19/10/2022"
    Quando o treinador tenta definir o capitão
    Então "Rodrigo" deve ser definido como capitão do "PSG" por ter mais tempo de clube

  Cenário: Empate total em todos os critérios
    Dado dois jogadores "Rodrigo" e "Vitinha"
    E ambos possuem contrato "ativo" com o "PSG"
    E ambos têm minutagem "constante"
    E ambos chegaram no dia "19/04/2022"
    Quando o treinador tenta definir o capitão
    Então o treinador deve escolher manualmente quem será o capitão do "PSG"
