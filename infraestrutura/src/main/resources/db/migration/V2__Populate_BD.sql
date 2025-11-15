-- ===========================
-- 1. COMPETIÇÃO
-- ===========================
INSERT INTO Competicao (id, nome, data_inicio, data_fim)
VALUES (1, 'Campeonato Brasileiro Série A', '2025-04-12', '2025-12-07');


-- ===========================
-- 2. USUÁRIOS
-- (Com a nova coluna 'funcao')
-- ===========================

-- Equipe A (Clube ID 1)
INSERT INTO Usuario (id, login, senha, nome, funcao) VALUES (1, 'tecnico_a', 'senha123', 'Abel Ferreira', 'TREINADOR');
INSERT INTO Usuario (id, login, senha, nome, funcao) VALUES (2, 'analista_a', 'senha123', 'Analista Clube A', 'ANALISTA');
INSERT INTO Usuario (id, login, senha, nome, funcao) VALUES (3, 'preparador_a', 'senha123', 'Preparador Clube A', 'PREPARADOR');

-- Equipe B (Clube ID 2)
INSERT INTO Usuario (id, login, senha, nome, funcao) VALUES (4, 'tecnico_b', 'senha123', 'Gabriel Milito', 'TREINADOR');
INSERT INTO Usuario (id, login, senha, nome, funcao) VALUES (5, 'analista_b', 'senha123', 'Analista Clube B', 'ANALISTA');
INSERT INTO Usuario (id, login, senha, nome, funcao) VALUES (6, 'preparador_b', 'senha123', 'Preparador Clube B', 'PREPARADOR');

-- Equipe C (Clube ID 3)
INSERT INTO Usuario (id, login, senha, nome, funcao) VALUES (7, 'tecnico_c', 'senha123', 'Artur Jorge', 'TREINADOR');
INSERT INTO Usuario (id, login, senha, nome, funcao) VALUES (8, 'analista_c', 'senha123', 'Analista Clube C', 'ANALISTA');
INSERT INTO Usuario (id, login, senha, nome, funcao) VALUES (9, 'preparador_c', 'senha123', 'Preparador Clube C', 'PREPARADOR');


-- ===========================
-- 3. CLUBES
-- (Com FKs de Usuario corrigidas e 'capitao' inicial NULL)
-- ===========================
INSERT INTO Clube (id, id_treinador, id_analista, id_preparador, id_competicao, nome, cidade_estado, estadio, capitao)
VALUES (1, 1, 2, 3, 1, 'Clube A (Palmeiras)', 'São Paulo, SP', 'Allianz Parque', NULL);

INSERT INTO Clube (id, id_treinador, id_analista, id_preparador, id_competicao, nome, cidade_estado, estadio, capitao)
VALUES (2, 4, 5, 6, 1, 'Clube B (Atlético-MG)', 'Belo Horizonte, MG', 'Arena MRV', NULL);

INSERT INTO Clube (id, id_treinador, id_analista, id_preparador, id_competicao, nome, cidade_estado, estadio, capitao)
VALUES (3, 7, 8, 9, 1, 'Clube C (Botafogo)', 'Rio de Janeiro, RJ', 'Nilton Santos', NULL);

INSERT INTO Clube (id, id_treinador, id_analista, id_preparador, id_competicao, nome, cidade_estado, estadio, capitao)
VALUES (4, NULL, NULL, NULL, 1, 'Passes Livres', 'N/A', 'N/A', NULL);


-- ===========================
-- 4. CONTRATOS
-- (Com 'id_clube' preenchido)
-- ===========================
INSERT INTO Contrato (id, duracao_meses, salario, status, id_clube) VALUES (1, 36, 150000.00, 'ATIVO', 1);
INSERT INTO Contrato (id, duracao_meses, salario, status, id_clube) VALUES (2, 24, 80000.00, 'ATIVO', 2);
INSERT INTO Contrato (id, duracao_meses, salario, status, id_clube) VALUES (3, 48, 120000.00, 'ATIVO', 3);
INSERT INTO Contrato (id, duracao_meses, salario, status, id_clube) VALUES (4, 12, 75000.00, 'ATIVO', 2); -- Contrato vencendo
INSERT INTO Contrato (id, duracao_meses, salario, status, id_clube) VALUES (5, 0, 0.00, 'EXPIRADO', 4); -- Contrato de agente livre


-- ===========================
-- 5. JOGADORES
-- (Com TODAS as colunas preenchidas)
-- ===========================

-- Jogador 1 (Clube A): Saudável, Capitão, Ativo
INSERT INTO Jogador (id, id_contrato, id_clube, id_competicao, nome, idade, posicao, perna, nota, jogos, gols, assistencias, status, minutagem, chegada_no_clube, capitao, grau_lesao, contrato_ativo, saudavel, desvio_padrao)
VALUES (1, 1, 1, 1, 'Gustavo Gómez', 31, 'Zagueiro', 'Direita', 8.5, 20, 2, 0, 'Disponível', 'Constante', '2022-03-15', true, -1, true, true, 0.5);

-- Jogador 2 (Clube B): Saudável, Ativo
INSERT INTO Jogador (id, id_contrato, id_clube, id_competicao, nome, idade, posicao, perna, nota, jogos, gols, assistencias, status, minutagem, chegada_no_clube, capitao, grau_lesao, contrato_ativo, saudavel, desvio_padrao)
VALUES (2, 2, 2, 1, 'Paulinho', 24, 'Atacante', 'Direita', 8.2, 22, 10, 3, 'Disponível', 'Constante', '2023-01-01', false, -1, true, true, 0.8);

-- Jogador 3 (Clube C): LESIONADO
INSERT INTO Jogador (id, id_contrato, id_clube, id_competicao, nome, idade, posicao, perna, nota, jogos, gols, assistencias, status, minutagem, chegada_no_clube, capitao, grau_lesao, contrato_ativo, saudavel, desvio_padrao)
VALUES (3, 3, 3, 1, 'Tiquinho Soares', 33, 'Atacante', 'Direita', 8.0, 18, 9, 1, 'Indisponível', 'Constante', '2021-11-20', false, 2, true, false, 0.7);

-- Jogador 4 (Clube B): SUSPENSO e VENCENDO EM BREVE
INSERT INTO Jogador (id, id_contrato, id_clube, id_competicao, nome, idade, posicao, perna, nota, jogos, gols, assistencias, status, minutagem, chegada_no_clube, capitao, grau_lesao, contrato_ativo, saudavel, desvio_padrao)
VALUES (4, 4, 2, 1, 'Zaracho', 26, 'Meio-campo', 'Direita', 8.1, 15, 3, 4, 'Indisponível', 'Constante', '2025-01-10', false, -1, true, true, 1.1); -- Chegou 10 meses atrás (contrato de 12 meses)

-- Jogador 5 (Passes Livres): Agente Livre
INSERT INTO Jogador (id, id_contrato, id_clube, id_competicao, nome, idade, posicao, perna, nota, jogos, gols, assistencias, status, minutagem, chegada_no_clube, capitao, grau_lesao, contrato_ativo, saudavel, desvio_padrao)
VALUES (5, 5, 4, 1, 'Kieza (Livre)', 35, 'Atacante', 'Direita', 7.0, 0, 0, 0, 'Disponível', 'Inconstante', '2020-01-01', false, -1, false, true, 1.5);


-- ===========================
-- 6. ATUALIZAÇÕES DE FK (Chaves Estrangeiras)
-- ===========================
-- Agora que o Jogador 1 existe, define-o como capitão do Clube 1
UPDATE Clube SET capitao = 1 WHERE id = 1;


-- ===========================
-- 7. DADOS DE LESÃO
-- (Para o Jogador 3)
-- ===========================
INSERT INTO Lesao (id_jogador, lesionado, tempo, plano, grau)
VALUES (3, true, '4 semanas', 'Fisioterapia intensiva no tornozelo.', 2);


-- ===========================
-- 8. DADOS DE SUSPENSÃO
-- (Para o Jogador 4)
-- ===========================
INSERT INTO Suspensao (id_jogador, suspenso, amarelo, vermelho)
VALUES (4, true, 3, 0);


-- ===========================
-- 9. DADOS DE PROPOSTA
-- (Para testar a aba "Propostas de Contratação")
-- ===========================

-- Proposta PENDENTE (Clube A quer Jogador 2 do Clube B)
INSERT INTO Proposta (id_propositor, id_receptor, id_jogador, status, valor, data)
VALUES (1, 2, 2, 'PENDENTE', 10000000.00, '2025-11-10 10:00:00');

-- Proposta RECUSADA (Clube C quis Jogador 1 do Clube A)
INSERT INTO Proposta (id_propositor, id_receptor, id_jogador, status, valor, data)
VALUES (3, 1, 1, 'RECUSADA', 15000000.00, '2025-11-01 15:00:00');

-- Proposta PENDENTE (Clube B quer Jogador 5, o Agente Livre)
INSERT INTO Proposta (id_propositor, id_receptor, id_jogador, status, valor, data)
VALUES (2, 4, 5, 'PENDENTE', 500000.00, '2025-11-12 11:00:00');


-- ===========================
-- 10. DADOS DE PARTIDA E NOTA
-- ===========================
INSERT INTO Partida (id, id_clube_casa, id_clube_visitante, data_jogo, hora, placar_clube_casa, placar_clube_visitante)
VALUES (1, 1, 2, '2025-11-09', '16:00:00', 2, 1);

INSERT INTO Nota (jogo_id, jogador_id, nota, observacao)
VALUES ('JOGO-1', '1', 9.0, 'Excelente performance defensiva, marcou um golo.');

INSERT INTO Nota (jogo_id, jogador_id, nota, observacao)
VALUES ('JOGO-1', '2', 7.5, 'Esforçado, mas perdeu algumas oportunidades.');

INSERT INTO Competicao (nome, data_inicio, data_fim) VALUES
('Copa do Brasil 2025', '2025-02-20', '2025-11-12'),
('Copa Libertadores 2025', '2025-02-05', '2025-11-29'),
('Copa Sul-Americana 2025', '2025-03-05', '2025-11-22'),
('Campeonato Paulista 2025', '2025-01-15', '2025-04-06'),
('Campeonato Carioca 2025', '2025-01-17', '2025-04-09'),
('Campeonato Mineiro 2025', '2025-01-21', '2025-04-05'),
('Copa do Nordeste 2025', '2025-01-22', '2025-05-10'),
('Supercopa do Brasil 2025', '2025-02-01', '2025-02-01'),
('Recopa Sul-Americana 2025', '2025-02-19', '2025-02-26'),
('Campeonato Pernambucano 2025', '2025-01-10', '2025-04-12');