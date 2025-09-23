# language: pt

Funcionalidade: Registro de cartões e suspensões dos atletas

Cenário: Jogador recebe primeiro cartão amarelo e permanece disponível
  Dado um atleta chamado "João"
  E o atleta possui contrato "ativo" com o clube
  E o atleta tem 0 cartões amarelos acumulados
  E o atleta tem 0 cartões vermelhos recebidos
  Quando o analista registra um cartão "amarelo" para o atleta
  Então o atleta deve ter 1 cartões amarelos acumulados
  E o atleta deve permanecer "disponível" para escalação

Cenário: Jogador recebe segundo cartão amarelo e permanece disponível
  Dado um atleta chamado "Pedro"
  E o atleta possui contrato "ativo" com o clube
  E o atleta tem 1 cartões amarelos acumulados
  E o atleta tem 0 cartões vermelhos recebidos
  Quando o analista registra um cartão "amarelo" para o atleta
  Então o atleta deve ter 2 cartões amarelos acumulados
  E o atleta deve permanecer "disponível" para escalação

Cenário: Jogador recebe terceiro cartão amarelo e fica suspenso
  Dado um atleta chamado "Carlos"
  E o atleta possui contrato "ativo" com o clube
  E o atleta tem 2 cartões amarelos acumulados
  E o atleta tem 0 cartões vermelhos recebidos
  Quando o analista registra um cartão "amarelo" para o atleta
  Então o atleta deve ter 3 cartões amarelos acumulados
  E o atleta deve ficar "suspenso" para a próxima escalação

Cenário: Jogador recebe cartão vermelho e fica suspenso imediatamente
  Dado um atleta chamado "Lucas"
  E o atleta possui contrato "ativo" com o clube
  E o atleta tem 0 cartões amarelos acumulados
  E o atleta tem 0 cartões vermelhos recebidos
  Quando o analista registra um cartão "vermelho" para o atleta
  Então o atleta deve ter 1 cartões vermelhos recebidos
  E o atleta deve ficar "suspenso" para a próxima escalação

Cenário: Jogador com cartão vermelho não acumula cartões amarelos para suspensão
  Dado um atleta chamado "Marcos"
  E o atleta possui contrato "ativo" com o clube
  E o atleta tem 2 cartões amarelos acumulados
  E o atleta tem 1 cartões vermelhos recebidos
  Quando o analista registra um cartão "amarelo" para o atleta
  Então o atleta deve ter 3 cartões amarelos acumulados
  E o atleta deve permanecer "suspenso" por conta do cartão vermelho

Cenário: Jogador já suspenso por cartões amarelos recebe cartão vermelho
  Dado um atleta chamado "Rafael"
  E o atleta possui contrato "ativo" com o clube
  E o atleta tem 3 cartões amarelos acumulados
  E o atleta tem 0 cartões vermelhos recebidos
  E o atleta está "suspenso" por acumulação de cartões amarelos
  Quando o analista registra um cartão "vermelho" para o atleta
  Então o atleta deve ter 1 cartões vermelhos recebidos
  E o atleta deve permanecer "suspenso" para a próxima escalação

Cenário: Atleta com contrato inativo não acumula suspensão por cartões
  Dado um atleta chamado "André"
  E o atleta possui contrato "inativo" com o clube
  E o atleta tem 2 cartões amarelos acumulados
  E o atleta tem 0 cartões vermelhos recebidos
  Quando o analista registra um cartão "amarelo" para o atleta
  Então o atleta deve ter 3 cartões amarelos acumulados
  E o atleta deve permanecer "indisponível" por conta do contrato inativo

Cenário: Atleta após suspensão tem cartões zerados e volta a ser disponível
  Dado um atleta chamado "Thiago"
  E o atleta possui contrato "ativo" com o clube
  E o atleta tem 3 cartões amarelos acumulados
  E o atleta tem 0 cartões vermelhos recebidos
  E o atleta está "suspenso" por acumulação de cartões amarelos
  Quando o analista executa o processo de limpeza de suspensões
  Então o atleta deve ter 0 cartões amarelos acumulados
  E o atleta deve voltar a ficar "disponível" para escalação

Cenário: Atleta recebe cartão amarelo após limpeza de suspensão
  Dado um atleta chamado "Vitor"
  E o atleta possui contrato "ativo" com o clube
  E o atleta tem 0 cartões amarelos acumulados
  E o atleta tem 0 cartões vermelhos recebidos
  E a suspensão anterior do atleta foi limpa
  Quando o analista registra um cartão "amarelo" para o atleta
  Então o atleta deve ter 1 cartões amarelos acumulados
  E o atleta deve permanecer "disponível" para escalação