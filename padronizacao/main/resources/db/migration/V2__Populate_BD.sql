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

-- ==============================================================================
-- POPULANDO ELENCO DO CLUBE A (PALMEIRAS) - ID CLUBE: 1
-- (Já existe Jogador 1: Gustavo Gómez. Adicionando mais 15 para totalizar 16)
-- ==============================================================================

-- Contratos (IDs 6 a 20)
INSERT INTO Contrato (id, duracao_meses, salario, status, id_clube) VALUES 
(6, 24, 500000.00, 'ATIVO', 1), (7, 36, 300000.00, 'ATIVO', 1), (8, 12, 250000.00, 'ATIVO', 1),
(9, 48, 400000.00, 'ATIVO', 1), (10, 24, 350000.00, 'ATIVO', 1), (11, 36, 700000.00, 'ATIVO', 1),
(12, 24, 200000.00, 'ATIVO', 1), (13, 12, 150000.00, 'ATIVO', 1), (14, 36, 900000.00, 'ATIVO', 1),
(15, 48, 800000.00, 'ATIVO', 1), (16, 24, 250000.00, 'ATIVO', 1), (17, 36, 300000.00, 'ATIVO', 1),
(18, 12, 100000.00, 'ATIVO', 1), (19, 24, 550000.00, 'ATIVO', 1), (20, 36, 450000.00, 'ATIVO', 1);

-- Jogadores (IDs 6 a 20)
INSERT INTO Jogador (id, id_contrato, id_clube, id_competicao, nome, idade, posicao, perna, nota, jogos, gols, assistencias, status, minutagem, chegada_no_clube, capitao, grau_lesao, contrato_ativo, saudavel, desvio_padrao) VALUES
(6, 6, 1, 1, 'Weverton', 36, 'Goleiro', 'Direita', 7.5, 20, 0, 0, 'Disponível', 'Constante', '2018-01-01', false, -1, true, true, 0.4),
(7, 7, 1, 1, 'Marcos Rocha', 35, 'Lateral', 'Direita', 6.8, 15, 0, 2, 'Disponível', 'Rotação', '2019-01-01', false, -1, true, true, 0.6),
(8, 8, 1, 1, 'Murilo', 27, 'Zagueiro', 'Direita', 7.2, 18, 3, 0, 'Disponível', 'Constante', '2022-01-01', false, -1, true, true, 0.5),
(9, 9, 1, 1, 'Piquerez', 25, 'Lateral', 'Esquerda', 7.8, 19, 2, 4, 'Disponível', 'Constante', '2021-08-01', false, -1, true, true, 0.7),
(10, 10, 1, 1, 'Zé Rafael', 30, 'Meio-campo', 'Direita', 7.0, 17, 1, 1, 'Disponível', 'Constante', '2019-01-01', false, -1, true, true, 0.6),
(11, 11, 1, 1, 'Raphael Veiga', 28, 'Meio-campo', 'Esquerda', 8.0, 20, 8, 6, 'Disponível', 'Constante', '2017-01-01', false, -1, true, true, 0.9),
(12, 12, 1, 1, 'Gabriel Menino', 23, 'Meio-campo', 'Direita', 6.5, 12, 1, 1, 'Disponível', 'Rotação', '2020-01-01', false, -1, true, true, 1.2),
(13, 13, 1, 1, 'Richard Ríos', 23, 'Meio-campo', 'Direita', 6.9, 14, 2, 1, 'Disponível', 'Rotação', '2023-03-01', false, -1, true, true, 0.8),
(14, 14, 1, 1, 'Dudu', 32, 'Atacante', 'Direita', 7.9, 5, 1, 0, 'Disponível', 'Constante', '2015-01-01', false, -1, true, true, 0.7),
(15, 15, 1, 1, 'Rony', 29, 'Atacante', 'Direita', 7.1, 19, 5, 3, 'Disponível', 'Constante', '2020-01-01', false, -1, true, true, 0.8),
(16, 16, 1, 1, 'Flaco López', 23, 'Atacante', 'Esquerda', 7.4, 15, 6, 1, 'Disponível', 'Rotação', '2022-06-01', false, -1, true, true, 1.0),
(17, 17, 1, 1, 'Mayke', 31, 'Lateral', 'Direita', 7.0, 14, 0, 3, 'Disponível', 'Rotação', '2019-01-01', false, -1, true, true, 0.5),
(18, 18, 1, 1, 'Lomba', 37, 'Goleiro', 'Direita', 6.5, 2, 0, 0, 'Disponível', 'Reserva', '2022-01-01', false, -1, true, true, 0.3),
(19, 19, 1, 1, 'Estêvão', 17, 'Atacante', 'Esquerda', 7.6, 10, 3, 2, 'Disponível', 'Rotação', '2024-01-01', false, -1, true, true, 1.5),
(20, 20, 1, 1, 'Lázaro', 22, 'Atacante', 'Direita', 6.7, 8, 1, 0, 'Disponível', 'Reserva', '2024-02-01', false, -1, true, true, 0.9);


-- ==============================================================================
-- POPULANDO ELENCO DO CLUBE B (ATLÉTICO-MG) - ID CLUBE: 2
-- (Já existem Jogadores 2 e 4. Adicionando mais 13 para totalizar 15)
-- ==============================================================================

-- Contratos (IDs 21 a 33)
INSERT INTO Contrato (id, duracao_meses, salario, status, id_clube) VALUES 
(21, 36, 400000.00, 'ATIVO', 2), (22, 24, 600000.00, 'ATIVO', 2), (23, 48, 500000.00, 'ATIVO', 2),
(24, 12, 300000.00, 'ATIVO', 2), (25, 36, 250000.00, 'ATIVO', 2), (26, 24, 1200000.00, 'ATIVO', 2),
(27, 36, 800000.00, 'ATIVO', 2), (28, 12, 200000.00, 'ATIVO', 2), (29, 24, 350000.00, 'ATIVO', 2),
(30, 48, 300000.00, 'ATIVO', 2), (31, 36, 400000.00, 'ATIVO', 2), (32, 24, 250000.00, 'ATIVO', 2),
(33, 12, 150000.00, 'ATIVO', 2);

-- Jogadores (IDs 21 a 33)
INSERT INTO Jogador (id, id_contrato, id_clube, id_competicao, nome, idade, posicao, perna, nota, jogos, gols, assistencias, status, minutagem, chegada_no_clube, capitao, grau_lesao, contrato_ativo, saudavel, desvio_padrao) VALUES
(21, 21, 2, 1, 'Everson', 33, 'Goleiro', 'Direita', 7.4, 22, 0, 0, 'Disponível', 'Constante', '2020-01-01', false, -1, true, true, 0.5),
(22, 22, 2, 1, 'Guilherme Arana', 27, 'Lateral', 'Esquerda', 7.9, 20, 3, 5, 'Disponível', 'Constante', '2020-01-01', true, -1, true, true, 0.7), -- Capitão
(23, 23, 2, 1, 'Bruno Fuchs', 25, 'Zagueiro', 'Direita', 6.8, 15, 0, 0, 'Disponível', 'Constante', '2023-01-01', false, -1, true, true, 0.8),
(24, 24, 2, 1, 'Maurício Lemos', 28, 'Zagueiro', 'Direita', 6.5, 10, 1, 0, 'Disponível', 'Rotação', '2023-02-01', false, -1, true, true, 1.0),
(25, 25, 2, 1, 'Saravia', 30, 'Lateral', 'Direita', 6.7, 14, 0, 1, 'Disponível', 'Rotação', '2023-01-01', false, -1, true, true, 0.6),
(26, 26, 2, 1, 'Hulk', 37, 'Atacante', 'Esquerda', 8.8, 18, 12, 5, 'Disponível', 'Constante', '2021-02-01', false, -1, true, true, 0.6),
(27, 27, 2, 1, 'Gustavo Scarpa', 30, 'Meio-campo', 'Esquerda', 7.5, 21, 4, 7, 'Disponível', 'Constante', '2024-01-01', false, -1, true, true, 0.8),
(28, 28, 2, 1, 'Otávio', 29, 'Meio-campo', 'Direita', 7.0, 19, 0, 1, 'Disponível', 'Constante', '2022-01-01', false, -1, true, true, 0.4),
(29, 29, 2, 1, 'Battaglia', 32, 'Meio-campo', 'Direita', 6.9, 16, 1, 0, 'Disponível', 'Rotação', '2023-04-01', false, -1, true, true, 0.5),
(30, 30, 2, 1, 'Igor Gomes', 25, 'Meio-campo', 'Direita', 6.4, 12, 2, 2, 'Disponível', 'Reserva', '2023-01-01', false, -1, true, true, 0.9),
(31, 31, 2, 1, 'Alisson', 30, 'Meio-campo', 'Direita', 6.6, 10, 0, 1, 'Disponível', 'Rotação', '2021-01-01', false, -1, true, true, 0.7),
(32, 32, 2, 1, 'Vargas', 34, 'Atacante', 'Direita', 6.2, 8, 1, 0, 'Disponível', 'Reserva', '2020-11-01', false, -1, true, true, 1.2),
(33, 33, 2, 1, 'Alan Kardec', 35, 'Atacante', 'Direita', 6.0, 5, 0, 0, 'Disponível', 'Reserva', '2022-06-01', false, -1, true, true, 1.0);


-- ==============================================================================
-- POPULANDO ELENCO DO CLUBE C (BOTAFOGO) - ID CLUBE: 3
-- (Já existe Jogador 3: Tiquinho (Lesionado). Adicionando mais 14 para totalizar 15)
-- ==============================================================================

-- Contratos (IDs 34 a 47)
INSERT INTO Contrato (id, duracao_meses, salario, status, id_clube) VALUES 
(34, 24, 300000.00, 'ATIVO', 3), (35, 36, 450000.00, 'ATIVO', 3), (36, 12, 200000.00, 'ATIVO', 3),
(37, 48, 400000.00, 'ATIVO', 3), (38, 24, 350000.00, 'ATIVO', 3), (39, 36, 600000.00, 'ATIVO', 3),
(40, 24, 300000.00, 'ATIVO', 3), (41, 12, 250000.00, 'ATIVO', 3), (42, 36, 500000.00, 'ATIVO', 3),
(43, 48, 900000.00, 'ATIVO', 3), (44, 24, 350000.00, 'ATIVO', 3), (45, 36, 400000.00, 'ATIVO', 3),
(46, 12, 150000.00, 'ATIVO', 3), (47, 24, 200000.00, 'ATIVO', 3);

-- Jogadores (IDs 34 a 47)
INSERT INTO Jogador (id, id_contrato, id_clube, id_competicao, nome, idade, posicao, perna, nota, jogos, gols, assistencias, status, minutagem, chegada_no_clube, capitao, grau_lesao, contrato_ativo, saudavel, desvio_padrao) VALUES
(34, 34, 3, 1, 'Gatito Fernández', 36, 'Goleiro', 'Direita', 7.0, 15, 0, 0, 'Disponível', 'Rotação', '2017-01-01', true, -1, true, true, 0.6), -- Capitão
(35, 35, 3, 1, 'John', 28, 'Goleiro', 'Direita', 7.3, 18, 0, 0, 'Disponível', 'Constante', '2024-01-01', false, -1, true, true, 0.5),
(36, 36, 3, 1, 'Damián Suárez', 36, 'Lateral', 'Direita', 6.8, 12, 0, 2, 'Disponível', 'Rotação', '2024-02-01', false, -1, true, true, 0.7),
(37, 37, 3, 1, 'Marçal', 35, 'Lateral', 'Esquerda', 6.9, 14, 0, 1, 'Disponível', 'Rotação', '2022-07-01', false, -1, true, true, 0.6),
(38, 38, 3, 1, 'Alexander Barboza', 29, 'Zagueiro', 'Esquerda', 7.1, 16, 1, 0, 'Disponível', 'Constante', '2024-01-01', false, -1, true, true, 0.8),
(39, 39, 3, 1, 'Lucas Halter', 24, 'Zagueiro', 'Direita', 6.7, 15, 0, 0, 'Disponível', 'Constante', '2024-01-01', false, -1, true, true, 0.9),
(40, 40, 3, 1, 'Bastos', 32, 'Zagueiro', 'Direita', 7.2, 10, 2, 0, 'Disponível', 'Rotação', '2023-08-01', false, -1, true, true, 0.5),
(41, 41, 3, 1, 'Marlon Freitas', 29, 'Meio-campo', 'Direita', 7.4, 20, 1, 3, 'Disponível', 'Constante', '2023-01-01', false, -1, true, true, 0.4),
(42, 42, 3, 1, 'Tchê Tchê', 31, 'Meio-campo', 'Direita', 7.0, 18, 0, 2, 'Disponível', 'Constante', '2022-04-01', false, -1, true, true, 0.5),
(43, 43, 3, 1, 'Luiz Henrique', 23, 'Atacante', 'Esquerda', 8.1, 15, 4, 4, 'Disponível', 'Constante', '2024-02-01', false, -1, true, true, 1.1),
(44, 44, 3, 1, 'Júnior Santos', 29, 'Atacante', 'Direita', 7.8, 22, 8, 2, 'Disponível', 'Constante', '2022-08-01', false, -1, true, true, 1.3),
(45, 45, 3, 1, 'Savarino', 27, 'Atacante', 'Direita', 7.5, 12, 3, 3, 'Disponível', 'Rotação', '2024-01-01', false, -1, true, true, 0.8),
(46, 46, 3, 1, 'Jeffinho', 24, 'Atacante', 'Direita', 7.2, 8, 2, 1, 'Disponível', 'Reserva', '2024-01-01', false, -1, true, true, 1.0),
(47, 47, 3, 1, 'Eduardo', 34, 'Meio-campo', 'Direita', 7.3, 11, 2, 2, 'Disponível', 'Rotação', '2022-07-01', false, -1, true, true, 0.7);