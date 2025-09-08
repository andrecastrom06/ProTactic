Funcionalidade: 8 - Registro de cartões e suspensões de atletas

Como analista de desempenho
Quero registrar cartões e suspensões dos atletas
Para impedir escalações irregulares

Regra: Suspensão automática

O atleta com contrato ativo só ficará suspenso e indisponível para jogos caso:

tenha acumulado 3 cartões amarelos em jogos diferentes, ou

tenha recebido 1 cartão vermelho.

Contexto:

Dado que existe o elenco cadastrado com os jogadores:
| nome | contrato_ativo |
| João | sim |
| Pedro | sim |
| Lucas | sim |

Cenário: Jogador suspenso após 3 cartões amarelos em jogos diferentes

Dado que João recebeu 1 cartão amarelo no jogo "JOGO-1"
E João recebeu 1 cartão amarelo no jogo "JOGO-2"
E João recebeu 1 cartão amarelo no jogo "JOGO-3"
Quando o analista registrar o terceiro cartão amarelo de João
Então João fica suspenso automaticamente
E João passa a estar indisponível para escalações até cumprir suspensão

Cenário: Jogador suspenso após cartão vermelho

Dado que Pedro recebeu 1 cartão vermelho no jogo "JOGO-4"
Quando o analista registrar esse cartão vermelho para Pedro
Então Pedro fica suspenso automaticamente
E Pedro passa a estar indisponível para escalações até cumprir suspensão

Cenário: Jogador ainda disponível com 2 cartões amarelos

Dado que Lucas recebeu 1 cartão amarelo no jogo "JOGO-5"
E Lucas recebeu 1 cartão amarelo no jogo "JOGO-6"
Quando o analista consultar a situação disciplinar de Lucas
Então Lucas não está suspenso
E permanece disponível para escalações