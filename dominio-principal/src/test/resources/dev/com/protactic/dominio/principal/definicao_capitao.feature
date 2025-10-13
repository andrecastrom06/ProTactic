# language: pt

Funcionalidade: Definição de Capitão e Vice-Capitão

  Cenário: "Rodrigo" cumpre todos os requisitos para ser capitão do "PSG"
    Dado um jogador chamado "Rodrigo"
    E ele possui contrato "ativo" com o "PSG"
    E ele tem "4 anos" de clube
    E sua minutagem é "constante"
    Quando o treinador tenta definir "Rodrigo" como capitão
    Então "Rodrigo" deve ser definido como capitão do "PSG"

  Cenário: "Vitinha" não tem minutagem constante para ser capitão do "PSG"
    Dado um jogador chamado "Vitinha"
    E ele possui contrato "ativo" com o "PSG"
    E ele tem "4 anos" de clube
    E sua minutagem é "inconstante"
    Quando o treinador tenta definir "Vitinha" como capitão
    Então "Vitinha" não deve ser definido como capitão do "PSG"

  Cenário: "Miguel" tem menos de 1 ano de clube para ser capitão do "PSG"
    Dado um jogador chamado "Miguel"
    E ele possui contrato "ativo" com o "PSG"
    E ele tem "6 meses" de clube
    E sua minutagem é "constante"
    Quando o treinador tenta definir "Miguel" como capitão
    Então "Miguel" não deve ser definido como capitão do "PSG"

  Cenário: "Vinicius" não tem contrato ativo para ser capitão do "PSG"
    Dado um jogador chamado "Vinicius"
    E ele possui contrato "inativo" com o "PSG"
    E ele tem "4 anos" de clube
    E sua minutagem é "constante"
    Quando o treinador tenta definir "Vinicius" como capitão
    Então "Vinicius" não deve ser definido como capitão do "PSG"

  Cenário: "Thiagão" não cumpre os critérios de tempo de clube e minutagem para ser capitão
    Dado um jogador chamado "Thiagão"
    E ele possui contrato "ativo" com o "PSG"
    E ele tem "6 meses" de clube
    E sua minutagem é "inconstante"
    Quando o treinador tenta definir "Thiagão" como capitão
    Então "Thiagão" não deve ser definido como capitão do "PSG"

  Cenário: "João Lucas" não cumpre os critérios de contrato e tempo de clube para ser capitão
    Dado um jogador chamado "João Lucas"
    E ele possui contrato "inativo" com o "PSG"
    E ele tem "6 meses" de clube
    E sua minutagem é "constante"
    Quando o treinador tenta definir "João Lucas" como capitão
    Então "João Lucas" não deve ser definido como capitão do "PSG"

  Cenário: "André" não cumpre os critérios de contrato e minutagem para ser capitão
    Dado um jogador chamado "André"
    E ele possui contrato "inativo" com o "PSG"
    E ele tem "4 anos" de clube
    E sua minutagem é "inconstante"
    Quando o treinador tenta definir "André" como capitão
    Então "André" não deve ser definido como capitão do "PSG"

  Cenário: "Luiz Felipe" não cumpre nenhum dos critérios para ser capitão
    Dado um jogador chamado "Luiz Felipe"
    E ele possui contrato "inativo" com o "PSG"
    E ele tem "6 meses" de clube
    E sua minutagem é "inconstante"
    Quando o treinador tenta definir "Luiz Felipe" como capitão
    Então "Luiz Felipe" não deve ser definido como capitão do "PSG"