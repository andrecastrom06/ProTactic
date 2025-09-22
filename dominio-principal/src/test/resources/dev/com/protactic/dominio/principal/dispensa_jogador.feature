# language: pt

Funcionalidade: Abrir processos de dispensa/rescisão

  Cenário: "Kieza" está saudável e com contrato ativo no "Sport" mas o clube quer dispensá-lo
    Dado um jogador chamado "Kieza" com contrato ativo com o "Sport" 
    E o jogador está saudável
    Quando o analista do "Sport" solicitar a rescisão do seu contrato
    Então o clube do jogador deve ser "Passes Livres"

  Cenário: "Kieza" está machucado e com contrato ativo no "Sport" mas o clube quer dispensá-lo
    Dado um jogador chamado "Kieza" com contrato ativo com o "Sport" 
    E o jogador está machucado
    Quando o analista do "Sport" solicitar a rescisão do seu contrato
    Então o sistema deve bloquear a rescisão com a mensagem "Não é permitido dispensar jogadores que estão lesionados."