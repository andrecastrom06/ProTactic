CREATE DATABASE protactic;
USE protactic;

-- Tabela Pessoa
CREATE TABLE Pessoa (
    CPF VARCHAR(20) PRIMARY KEY,
    Nome VARCHAR(100) NOT NULL,
    Cidade VARCHAR(100) NOT NULL,
    Estado VARCHAR(100) NOT NULL,
    Pais VARCHAR(100) NOT NULL,
    RG VARCHAR(20) UNIQUE NOT NULL,
    Data_Nascimento DATE NOT NULL
);

-- Tabela Clube
CREATE TABLE Clube (
    CNPJ VARCHAR(20) PRIMARY KEY,
    Pais VARCHAR(100) NOT NULL,
    Estado VARCHAR(100) NOT NULL,
    Cidade VARCHAR(100) NOT NULL,
    Nome VARCHAR(100) NOT NULL,
    UNIQUE (Nome, Cidade, Estado)
);

-- Tabela Analista de Desempenho
CREATE TABLE Analista_de_desempenho (
    fk_Pessoa_CPF VARCHAR(20) PRIMARY KEY,
    fk_Clube_CNPJ VARCHAR(20),
    Matricula VARCHAR(20) NOT NULL UNIQUE,
    Ativo BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (fk_Pessoa_CPF) REFERENCES Pessoa(CPF),
    FOREIGN KEY (fk_Clube_CNPJ) REFERENCES Clube(CNPJ)
);

-- Tabela Empresário
CREATE TABLE Empresario (
    fk_Pessoa_CPF VARCHAR(20) PRIMARY KEY,
    Agencia VARCHAR(100) NOT NULL,
    Registro_FIFA VARCHAR(50) NOT NULL UNIQUE,
    FOREIGN KEY (fk_Pessoa_CPF) REFERENCES Pessoa(CPF)
);

-- Tabela Jogador
CREATE TABLE Jogador (
    fk_Pessoa_CPF VARCHAR(20) PRIMARY KEY,
    fk_Empresario_CPF VARCHAR(20),
    fk_Clube_CNPJ VARCHAR(20),
    Posicao VARCHAR(50) NOT NULL,
    Altura DECIMAL(5,2) CHECK (Altura > 0),
    Peso DECIMAL(5,2) CHECK (Peso > 0),
    Valor_estimado FLOAT CHECK (Valor_estimado >= 0),
    Perna_preferida VARCHAR(20) CHECK (Perna_preferida IN ('Direita', 'Esquerda')),
    Categoria VARCHAR(50) NOT NULL,
    FOREIGN KEY (fk_Pessoa_CPF) REFERENCES Pessoa(CPF),
    FOREIGN KEY (fk_Empresario_CPF) REFERENCES Empresario(fk_Pessoa_CPF),
    FOREIGN KEY (fk_Clube_CNPJ) REFERENCES Clube(CNPJ)
);

-- Tabela Sessao (generaliza Partida e Treino)
CREATE TABLE Sessao (
    ID VARCHAR(20) PRIMARY KEY,
    Tipo ENUM('Treino', 'Jogo') NOT NULL,
    Mandante VARCHAR(100),
    Visitante VARCHAR(100),
    Data DATE NOT NULL
);

-- Tabela Video (relacionada à Sessao)
CREATE TABLE Video (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    fk_Sessao_ID VARCHAR(20),
    URL TEXT NOT NULL,
    Tipo ENUM('Treino', 'Jogo') NOT NULL,
    Data_Envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Descricao TEXT,
    FOREIGN KEY (fk_Sessao_ID) REFERENCES Sessao(ID)
);

-- Tabela de desempenho de jogadores por Sessão
CREATE TABLE Desempenha (
    fk_Sessao_ID VARCHAR(20),
    fk_Jogador_CPF VARCHAR(20),
    Gols INTEGER CHECK (Gols >= 0),
    Assistencias INTEGER CHECK (Assistencias >= 0),
    Passe_curto_tentados INTEGER CHECK (Passe_curto_tentados >= 0),
    Passe_curto_acertados INTEGER CHECK (Passe_curto_acertados >= 0),
    Passe_longo_tentados INTEGER CHECK (Passe_longo_tentados >= 0),
    Passe_longo_acertados INTEGER CHECK (Passe_longo_acertados >= 0),
    Cruzamento_tentados INTEGER CHECK (Cruzamento_tentados >= 0),
    Cruzamento_acertados INTEGER CHECK (Cruzamento_acertados >= 0),
    Dominio_tentados INTEGER CHECK (Dominio_tentados >= 0),
    Dominio_acertados INTEGER CHECK (Dominio_acertados >= 0),
    Finalizacao_tentadas INTEGER CHECK (Finalizacao_tentadas >= 0),
    Finalizacao_acertadas INTEGER CHECK (Finalizacao_acertadas >= 0),
    Cabeceio_tentados INTEGER CHECK (Cabeceio_tentados >= 0),
    Cabeceios_acertados INTEGER CHECK (Cabeceios_acertados >= 0),
    Drible_tentados INTEGER CHECK (Drible_tentados >= 0),
    Drible_acertados INTEGER CHECK (Drible_acertados >= 0),
    Faltas_tentadas INTEGER CHECK (Faltas_tentadas >= 0),
    Faltas_acertadas INTEGER CHECK (Faltas_acertadas >= 0),
    Velocidade_media DECIMAL(5,2) CHECK (Velocidade_media >= 0),
    Velocidade_sprint DECIMAL(5,2) CHECK (Velocidade_sprint >= 0),
    Velocidade_reacao DECIMAL(5,2) CHECK (Velocidade_reacao >= 0),
    Impulsao DECIMAL(5,2) CHECK (Impulsao >= 0),
    Resistencia INTEGER CHECK (Resistencia >= 0),
    Forca INTEGER CHECK (Forca >= 0),
    Chute_longe_tentados INTEGER CHECK (Chute_longe_tentados >= 0),
    Chute_longe_acertados INTEGER CHECK (Chute_longe_acertados >= 0),
    Interceptacao_tentadas INTEGER CHECK (Interceptacao_tentadas >= 0),
    Interceptacao_acertadas INTEGER CHECK (Interceptacao_acertadas >= 0),
    Penaltis_tentados INTEGER CHECK (Penaltis_tentados >= 0),
    Penaltis_acertados INTEGER CHECK (Penaltis_acertados >= 0),
    Desarmes_tentados INTEGER CHECK (Desarmes_tentados >= 0),
    Desarmes_acertados INTEGER CHECK (Desarmes_acertados >= 0),
    Chutes_goleiro_defendidos INTEGER CHECK (Chutes_goleiro_defendidos >= 0),
    Chutes_goleiro_nao_defendidos INTEGER CHECK (Chutes_goleiro_nao_defendidos >= 0),
    Lancamento_com_mao_tentado INTEGER CHECK (Lancamento_com_mao_tentado >= 0),
    Lancamento_com_mao_acertado INTEGER CHECK (Lancamento_com_mao_acertado >= 0),
    PRIMARY KEY (fk_Sessao_ID, fk_Jogador_CPF),
    FOREIGN KEY (fk_Sessao_ID) REFERENCES Sessao(ID),
    FOREIGN KEY (fk_Jogador_CPF) REFERENCES Jogador(fk_Pessoa_CPF)
);

-- Tabela Analisa
CREATE TABLE Analisa (
    fk_Analista_CPF VARCHAR(20),
    fk_Jogador_CPF VARCHAR(20),
    PRIMARY KEY (fk_Analista_CPF, fk_Jogador_CPF),
    FOREIGN KEY (fk_Analista_CPF) REFERENCES Analista_de_desempenho(fk_Pessoa_CPF),
    FOREIGN KEY (fk_Jogador_CPF) REFERENCES Jogador(fk_Pessoa_CPF)
);

-- Tabela Relatório
CREATE TABLE Relatorio (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    fk_Jogador_CPF VARCHAR(20),
    Periodo_Inicio DATE NOT NULL,
    Periodo_Fim DATE NOT NULL,
    Data_Geracao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Observacoes TEXT,
    FOREIGN KEY (fk_Jogador_CPF) REFERENCES Jogador(fk_Pessoa_CPF)
);

-- TESTES POPULAR

-- Pessoas
INSERT INTO Pessoa (CPF, Nome, Cidade, Estado, Pais, RG, Data_Nascimento) VALUES
('11111111111', 'Lucas Silva', 'São Paulo', 'SP', 'Brasil', '123456789', '2003-05-12'),
('22222222222', 'Maria Santos', 'Rio de Janeiro', 'RJ', 'Brasil', '987654321', '1998-11-20'),
('33333333333', 'Pedro Almeida', 'Belo Horizonte', 'MG', 'Brasil', '456789123', '2005-07-30'),
('44444444444', 'Ana Costa', 'Curitiba', 'PR', 'Brasil', '321654987', '1990-02-10'),
('55555555555', 'Carlos Pereira', 'Porto Alegre', 'RS', 'Brasil', '654987321', '1985-12-15');

-- Clubes
INSERT INTO Clube (CNPJ, Pais, Estado, Cidade, Nome) VALUES
('12345678000101', 'Brasil', 'SP', 'São Paulo', 'São Paulo FC'),
('12345678000202', 'Brasil', 'RJ', 'Rio de Janeiro', 'Flamengo'),
('12345678000303', 'Brasil', 'MG', 'Belo Horizonte', 'Atlético MG');

-- Analistas de desempenho
INSERT INTO Analista_de_desempenho (fk_Pessoa_CPF, fk_Clube_CNPJ, Matricula, Ativo) VALUES
('44444444444', '12345678000101', 'ANA001', TRUE);

-- Empresarios
INSERT INTO Empresario (fk_Pessoa_CPF, Agencia, Registro_FIFA) VALUES
('55555555555', 'Global Sports', 'FIFA12345');

-- Jogadores
INSERT INTO Jogador (fk_Pessoa_CPF, fk_Empresario_CPF, fk_Clube_CNPJ, Posicao, Altura, Peso, Valor_estimado, Perna_preferida, Categoria) VALUES
('11111111111', '55555555555', '12345678000101', 'Meio-campo', 1.78, 72.5, 1000000, 'Direita', 'Sub-20'),
('22222222222', NULL, '12345678000202', 'Atacante', 1.65, 60.0, 1500000, 'Esquerda', 'Profissional'),
('33333333333', NULL, '12345678000303', 'Zagueiro', 1.85, 80.0, 700000, 'Direita', 'Sub-18');

-- Sessoes (Jogos/Treinos)
INSERT INTO Sessao (ID, Tipo, Mandante, Visitante, Data) VALUES
('S001', 'Jogo', 'São Paulo FC', 'Flamengo', '2025-06-10'),
('S002', 'Treino', NULL, NULL, '2025-06-08');

-- Videos
INSERT INTO Video (fk_Sessao_ID, URL, Tipo, Descricao) VALUES
('S001', 'https://videos.example.com/s001_jogo.mp4', 'Jogo', 'Partida oficial do campeonato estadual'),
('S002', 'https://videos.example.com/s002_treino.mp4', 'Treino', 'Treino técnico focado em passes');

-- Desempenha (estatísticas do jogador na sessão)
INSERT INTO Desempenha (
    fk_Sessao_ID, fk_Jogador_CPF, Gols, Assistencias, Passe_curto_tentados, Passe_curto_acertados, Passe_longo_tentados, Passe_longo_acertados,
    Cruzamento_tentados, Cruzamento_acertados, Dominio_tentados, Dominio_acertados, Finalizacao_tentadas, Finalizacao_acertadas,
    Cabeceio_tentados, Cabeceios_acertados, Drible_tentados, Drible_acertados, Faltas_tentadas, Faltas_acertadas,
    Velocidade_media, Velocidade_sprint, Velocidade_reacao, Impulsao, Resistencia, Forca,
    Chute_longe_tentados, Chute_longe_acertados, Interceptacao_tentadas, Interceptacao_acertadas,
    Penaltis_tentados, Penaltis_acertados, Desarmes_tentados, Desarmes_acertados,
    Chutes_goleiro_defendidos, Chutes_goleiro_nao_defendidos, Lancamento_com_mao_tentado, Lancamento_com_mao_acertado
) VALUES
('S001', '11111111111', 1, 2, 25, 22, 5, 4, 3, 2, 10, 9, 4, 3, 1, 1, 7, 5, 2, 1, 7.5, 32.1, 0.35, 0.50, 8, 7, 3, 2, 6, 5, 0, 0, 4, 4, 1, 0, 3, 2),
('S002', '11111111111', 0, 1, 30, 27, 8, 6, 5, 3, 12, 11, 2, 2, 0, 0, 10, 8, 1, 1, 7.0, 31.5, 0.40, 0.55, 7, 8, 4, 3, 7, 7, 0, 0, 5, 4, 2, 1, 4, 3);

-- Analisa (relaciona analistas e jogadores)
INSERT INTO Analisa (fk_Analista_CPF, fk_Jogador_CPF) VALUES
('44444444444', '11111111111');

-- Relatorio (relatório de progresso)
INSERT INTO Relatorio (fk_Jogador_CPF, Periodo_Inicio, Periodo_Fim, Observacoes) VALUES
('11111111111', '2025-05-01', '2025-06-10', 'Boa evolução no passe curto e resistência.');