# language: pt

Funcionalidade: 7 - Definição de Capitão

  Regra:
  Para ser capitão, o jogador precisa atender a todos os requisitos necessários, que são: mínimo de 1 ano de clube, e contrato ativo. 
  A escolha do jogador que será o capitão do time será definida exclusivamente pelo treinador, no qual ele escolherá os jogador que será o capitão,
  considerando que ele cumpre os requisitos mínimos para ser capitão do time.

  Contexto: Estamos no dia "19/10/2025"

  Cenário: "Rodrigo" cumpre todos os requisitos para ser capitão do "PSG"
    Dado um jogador chamado "Rodrigo"
    E ele possui contrato "ativo" com o "PSG"
    E ele chegou no dia "18/10/2024" no clube
    Quando o treinador tenta definir "Rodrigo" como capitão
    Então "Rodrigo" deve ser definido como capitão do "PSG"

  Cenário: "Miguel" tem menos de 1 ano de clube para ser capitão do "PSG"
    Dado um jogador chamado "Miguel"
    E ele possui contrato "ativo" com o "PSG"
    E ele chegou no dia "19/04/2025" no clube
    Quando o treinador tenta definir "Miguel" como capitão
    Então "Miguel" não deve ser definido como capitão do "PSG"

  Cenário: "Vinicius" não tem contrato ativo para ser capitão do "PSG"
    Dado um jogador chamado "Vinicius"
    E ele possui contrato "inativo" com o "PSG"
    E ele chegou no dia "19/10/2021" no clube
    Quando o treinador tenta definir "Vinicius" como capitão
    Então "Vinicius" não deve ser definido como capitão do "PSG"

  Cenário: "Luiz Felipe" não cumpre nenhum dos critérios para ser capitão
    Dado um jogador chamado "Luiz Felipe"
    E ele possui contrato "inativo" com o "PSG"
    E ele chegou no dia "19/04/2025" no clube
    Quando o treinador tenta definir "Luiz Felipe" como capitão
    Então "Luiz Felipe" não deve ser definido como capitão do "PSG"

  Cenário: Mais de um jogador cumpre os requisitos
    Dado dois jogadores "Rodrigo" e "Vitinha"
    E ambos possuem contrato "ativo" com o "PSG"
    E ambos chegaram no dia "19/04/2022"
    Quando o treinador tenta definir o capitão
    Então o treinador deve escolher manualmente quem será o capitão do "PSG"
