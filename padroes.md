# Padrões de Projeto Adotados

Abaixo estão listados os padrões de projeto (GoF e Arquiteturais/Enterprise) adotados no projeto ProTactic, juntamente com as classes criadas e/ou alteradas por conta de sua adoção no módulo `dominio-principal` (que contém a lógica de domínio) e nos módulos de infraestrutura (`infraestrutura`) e aplicação (`aplicacao`).

## Padrões Comportamentais (Behavioral GoF)

| Padrão de Projeto | Descrição | Classes Envolvidas |
| :--- | :--- | :--- |
| **Strategy** | Define uma família de algoritmos (cálculo de carga semanal), encapsula cada um deles e os torna intercambiáveis. Permite alternar a regra de cálculo em tempo de execução. | `dominio-principal/feature_02_carga_semanal/calculo/CalculadoraCargaStrategy.java` (Interface Strategy)<br/>`dominio-principal/feature_02_carga_semanal/calculo/CalculoCargaExponencial.java` (Strategy Concreta)<br/>`dominio-principal/feature_02_carga_semanal/calculo/CalculoCargaLinear.java` (Strategy Concreta) |
| **Observer** | Define uma dependência um-para-muitos entre objetos, para que alterações no "Subject" (registro de lesão) notifiquem os "Observers". | `dominio-principal/feature_03_registro_lesao/observer/LesaoObserver.java` (Interface Observer)<br/>`dominio-principal/feature_03_registro_lesao/servico/RegistroLesoesServico.java` (Subject, onde a notificação é gerenciada ou disparada) |
| **Iterator** | Fornece uma maneira de acessar sequencialmente os elementos de um objeto agregado (`Escalacao`) sem expor sua representação subjacente. | `dominio-principal/feature_04_esquema_escalacao/entidade/Escalacao.java` (Classe Agregada)<br/>`dominio-principal/feature_04_esquema_escalacao/iterator/EscalacaoIterator.java` (Iterador Concreto)<br/>`dominio-principal/feature_04_esquema_escalacao/iterator/JogadorIterator.java` (Interface/Iterador Abstrato) |
| **Template Method** | Define o esqueleto de um algoritmo (geração de proposta de contrato) em uma classe abstrata, adiando a implementação de algumas etapas para subclasses. | `dominio-principal/feature_05_proposta_contratacao/template/GeradorProposta.java` (Classe Abstrata do Template)<br/>`dominio-principal/feature_05_proposta_contratacao/template/GeradorPropostaBase.java` (Classe Abstrata do Template)<br/>`dominio-principal/feature_05_proposta_contratacao/template/GeradorPropostaEstrela.java` (Implementação Concreta do Template) |

## Padrões Estruturais (Structural GoF)

| Padrão de Projeto | Descrição | Classes Envolvidas |
| :--- | :--- | :--- |
| **Decorator** | Anexa responsabilidades adicionais a um objeto dinamicamente (cálculo de premiação), oferecendo uma alternativa flexível à herança. | `dominio-principal/feature_11_premiacao_interna/decorator/CalculadoraPremiacao.java` (Componente/Interface)<br/>`dominio-principal/feature_11_premiacao_interna/decorator/PremiacaoBase.java` (Componente Concreto)<br/>`dominio-principal/feature_11_premiacao_interna/decorator/BonusCapitaoDecorator.java` (Decorador Concreto)<br/>`dominio-principal/feature_11_premiacao_interna/decorator/BonusVitoriaDecorator.java` (Decorador Concreto) |
| **Proxy** | Fornece um substituto (`Proxy`) para controlar o acesso a outro objeto (`DispensaService`), possivelmente para adicionar lógica de segurança, logging, ou lazy loading. | `dominio-principal/feature_12_dispensa_rescisao/servico/IDispensaService.java` (Interface Subject)<br/>`dominio-principal/feature_12_dispensa_rescisao/servico/DispensaService.java` (Real Subject)<br/>`dominio-principal/feature_12_dispensa_rescisao/servico/DispensaServiceProxy.java` (Proxy) |

## Padrões Arquiteturais/Enterprise

| Padrão de Projeto | Descrição | Classes Envolvidas |
| :--- | :--- | :--- |
| **Repository** | Medeia entre o domínio e a camada de persistência, simulando uma coleção de objetos de domínio. | Interfaces `*Repository.java` em `dominio-principal/repositorio/` (Ex: `JogadorRepository.java`)<br/>Classes de implementação `*RepositoryImpl.java` e `*RepositorySpringData.java` em `infraestrutura/persistencia/jpa/` |
| **Service Layer** | Define um conjunto de operações de aplicação e coordena a lógica de negócios em vez de apenas delegar para o domínio. | Classes `*Service.java` em `dominio-principal/servico/` (Lógica de Negócio)<br/>Classes `*ServicoAplicacao.java` em `aplicacao/principal/` (Camada de Aplicação/Orquestração) |
