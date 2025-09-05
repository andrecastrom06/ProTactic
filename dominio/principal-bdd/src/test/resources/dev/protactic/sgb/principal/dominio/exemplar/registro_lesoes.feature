# language: pt

Funcionalidade: 3 - Registro de lesões e planejamento de recuperações
  Como gestor do elenco
  Quero registrar lesões e planejar recuperações
  Para controlar disponibilidade de atletas

  Regra: Registro e planejamento condicionados
    O atleta precisa ter contrato ativo para registrar lesão.
    É preciso ter lesão registrada para planejar recuperação.
    Não é permitido registrar nova lesão enquanto houver lesão ativa.

  Contexto:
    Dado que existe um atleta identificado por "AT-001"

  Cenário: Registro de lesão de um atleta com contrato ativo no clube
    Dado que o atleta "AT-001" está saudável e com contrato ativo
    Quando registrar uma lesão de grau 2 para o atleta "AT-001"
    Então a lesão é registrada com grau 2 e status "ativa"
    E o status do atleta passa a ser "Lesionado (grau 2)"
    E a disponibilidade do atleta para jogos e treinos fica "indisponível"

  Cenário: Registro de lesão de um atleta sem contrato ativo no clube
    Dado que o atleta "AT-001" está saudável e sem contrato ativo
    Quando tentar registrar uma lesão de grau 2 para o atleta "AT-001"
    Então a lesão não é registrada
    E o sistema exibe o erro "Contrato inativo impede o registro de lesão"

  Cenário: Planejamento para recuperação de uma lesão leve de um atleta
    Dado que o atleta "AT-001" possui uma lesão de grau 1 registrada e ativa
    Quando cadastrar um plano de recuperação com treinos adaptados por 14 dias
    Então o plano de recuperação é registrado para o atleta "AT-001"
    E a permissão de treino do atleta fica "limitada" até o fim do plano

  Cenário: Registro de múltiplas lesões simultâneas
    Dado que o atleta "AT-001" possui uma lesão de grau 3 registrada e ativa
    Quando tentar registrar uma nova lesão para o atleta "AT-001"
    Então a nova lesão não é registrada
    E o sistema solicita finalizar a recuperação da lesão ativa de grau 3