# language: pt

Funcionalidade: 2 - Planejar carga semanal de treino

  Regra: O atleta deve estar sem lesão ou com lesão de grau 0 (desconforto) 
         e possuir contrato ativo para que seja possível registrar carga semanal.

  Contexto:
    Dado que o preparador físico está na tela de planejamento de cargas semanais

  Cenário: Cadastrar carga de jogador lesionado (grau 3)
    Dado que o jogador "Carlos" tem uma lesão de grau 3
    E "Carlos" possui contrato ativo
    Quando o preparador físico tentar registrar seu treinamento
    Então "Carlos" não poderá ter o treino registrado

  Cenário: Cadastrar carga de jogador lesionado (grau 2)
    Dado que o jogador "Rafael" tem uma lesão de grau 2
    E "Rafael" possui contrato ativo
    Quando o preparador físico tentar registrar seu treinamento
    Então "Rafael" não poderá ter o treino registrado

  Cenário: Cadastrar carga de jogador lesionado (grau 1)
    Dado que o jogador "Pedro" tem uma lesão de grau 1
    E "Pedro" possui contrato ativo
    Quando o preparador físico tentar registrar seu treinamento
    Então "Pedro" não poderá ter o treino registrado

  Cenário: Cadastrar carga de jogador saudável
    Dado que o jogador "João" está saudável
    E "João" possui contrato ativo
    Quando o preparador físico criar seus treinos semanais
    Então o treino será registrado na aba de "João"

  Cenário: Cadastrar carga de jogador lesionado (grau 0)
    Dado que o jogador "Lucas" tem uma lesão de grau 0
    E "Lucas" possui contrato ativo
    Quando o preparador físico criar seus treinos semanais
    Então o treino será registrado na aba de "Lucas"

  Cenário: Cadastrar carga de jogador sem contrato ativo
    Dado que o jogador "Marcos" está saudável
    E "Marcos" não possui contrato ativo
    Quando o preparador físico tentar registrar seu treinamento
    Então "Marcos" não poderá ter o treino registrado

  Cenário: Cadastrar carga de jogador com contrato ativo
    Dado que o jogador "Thiago" está saudável
    E "Thiago" possui contrato ativo
    Quando o preparador físico tentar registrar seu treinamento
    Então o treino será registrado na aba de "Thiago"

  Esquema do Cenário: Tentar cadastrar treino para jogadores com condições inválidas
    Dado que o jogador <nome> tem uma lesão de grau <grau_lesao>
    E <nome> possui contrato <status_contrato>
    Quando o preparador físico tentar registrar seu treinamento
    Então <nome> não poderá ter o treino registrado

    Exemplos:
      | nome       | grau_lesao | status_contrato |
      | "Diego"    | 1          | ativo           |
      | "Felipe"   | 2          | ativo           |
      | "Marcelo"  | 3          | ativo           |
      | "Anderson" | 0          | inativo         |
