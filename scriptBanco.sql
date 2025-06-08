CREATE DATABASE protactic;
USE protactic;

CREATE TABLE Pessoa (
    CPF VARCHAR(20) PRIMARY KEY,
    Nome VARCHAR(100) NOT NULL,
    Cidade VARCHAR(100) NOT NULL,
    Estado VARCHAR(100) NOT NULL,
    Pais VARCHAR(100) NOT NULL,
    RG VARCHAR(20) UNIQUE NOT NULL,
    Data_Nascimento DATE NOT NULL
);

CREATE TABLE Clube (
    CNPJ VARCHAR(20) PRIMARY KEY,
    Pais VARCHAR(100) NOT NULL,
    Estado VARCHAR(100) NOT NULL,
    Cidade VARCHAR(100) NOT NULL,
    Nome VARCHAR(100) NOT NULL,
    UNIQUE (Nome, Cidade, Estado)
);

CREATE TABLE Analista_de_desempenho (
    fk_Pessoa_CPF VARCHAR(20) PRIMARY KEY,
    fk_Clube_CNPJ VARCHAR(20),
    Matricula VARCHAR(20) NOT NULL UNIQUE,
    Ativo BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (fk_Pessoa_CPF) REFERENCES Pessoa(CPF),
    FOREIGN KEY (fk_Clube_CNPJ) REFERENCES Clube(CNPJ)
);

CREATE TABLE Empresario (
    fk_Pessoa_CPF VARCHAR(20) PRIMARY KEY,
    Agencia VARCHAR(100) NOT NULL,
    Registro_FIFA VARCHAR(50) NOT NULL UNIQUE,
    FOREIGN KEY (fk_Pessoa_CPF) REFERENCES Pessoa(CPF)
);

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

CREATE TABLE Partida (
    ID VARCHAR(20) PRIMARY KEY,
    Mandante VARCHAR(100) NOT NULL,
    Visitante VARCHAR(100) NOT NULL,
    Data DATE NOT NULL
);

CREATE TABLE Desempenha (
    fk_Partida_ID VARCHAR(20),
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
    PRIMARY KEY (fk_Partida_ID, fk_Jogador_CPF),
    FOREIGN KEY (fk_Partida_ID) REFERENCES Partida(ID),
    FOREIGN KEY (fk_Jogador_CPF) REFERENCES Jogador(fk_Pessoa_CPF)
);

CREATE TABLE Analisa (
    fk_Analista_CPF VARCHAR(20),
    fk_Jogador_CPF VARCHAR(20),
    PRIMARY KEY (fk_Analista_CPF, fk_Jogador_CPF),
    FOREIGN KEY (fk_Analista_CPF) REFERENCES Analista_de_desempenho(fk_Pessoa_CPF),
    FOREIGN KEY (fk_Jogador_CPF) REFERENCES Jogador(fk_Pessoa_CPF)
);

-- Populando TESTE

INSERT INTO Pessoa (CPF, Nome, Cidade, Estado, Pais, RG, Data_Nascimento) VALUES
('11111111111', 'Carlos Silva', 'São Paulo', 'SP', 'Brasil', 'MG1234567', '1990-05-12'),
('22222222222', 'Ana Souza', 'Rio de Janeiro', 'RJ', 'Brasil', 'RJ2345678', '1985-08-23'),
('33333333333', 'Marcos Lima', 'Belo Horizonte', 'MG', 'Brasil', 'MG3456789', '1995-03-14'),
('44444444444', 'Paula Mendes', 'Porto Alegre', 'RS', 'Brasil', 'RS4567890', '1992-11-05'),
('55555555555', 'Rafael Costa', 'Curitiba', 'PR', 'Brasil', 'PR5678901', '2000-07-30');

INSERT INTO Clube (CNPJ, Pais, Estado, Cidade, Nome) VALUES
('12345678000100', 'Brasil', 'SP', 'São Paulo', 'São Paulo FC'),
('98765432000199', 'Brasil', 'RJ', 'Rio de Janeiro', 'Flamengo');

INSERT INTO Analista_de_desempenho (fk_Pessoa_CPF, fk_Clube_CNPJ, Matricula, Ativo) VALUES
('11111111111', '12345678000100', 'A001', TRUE),
('22222222222', '98765432000199', 'A002', TRUE);

INSERT INTO Empresario (fk_Pessoa_CPF, Agencia, Registro_FIFA) VALUES
('33333333333', 'Top Sports', 'FIFA-99999');

INSERT INTO Jogador (fk_Pessoa_CPF, fk_Empresario_CPF, fk_Clube_CNPJ, Posicao, Altura, Peso, Valor_estimado, Perna_preferida, Categoria) VALUES
('44444444444', '33333333333', '12345678000100', 'Atacante', 1.80, 75.5, 1000000, 'Direita', 'Profissional'),
('55555555555', '33333333333', '98765432000199', 'Meio-Campo', 1.75, 70.0, 800000, 'Esquerda', 'Profissional');

INSERT INTO Partida (ID, Mandante, Visitante, Data) VALUES
('P001', 'São Paulo FC', 'Flamengo', '2025-06-01'),
('P002', 'Flamengo', 'São Paulo FC', '2025-06-07');

INSERT INTO Desempenha (
    fk_Partida_ID, fk_Jogador_CPF, Gols, Assistencias, Passe_curto_tentados, Passe_curto_acertados,
    Passe_longo_tentados, Passe_longo_acertados, Cruzamento_tentados, Cruzamento_acertados,
    Dominio_tentados, Dominio_acertados, Finalizacao_tentadas, Finalizacao_acertadas,
    Cabeceio_tentados, Cabeceios_acertados, Drible_tentados, Drible_acertados,
    Faltas_tentadas, Faltas_acertadas, Velocidade_media, Velocidade_sprint, Velocidade_reacao,
    Impulsao, Resistencia, Forca, Chute_longe_tentados, Chute_longe_acertados,
    Interceptacao_tentadas, Interceptacao_acertadas, Penaltis_tentados, Penaltis_acertados,
    Desarmes_tentados, Desarmes_acertados, Chutes_goleiro_defendidos, Chutes_goleiro_nao_defendidos,
    Lancamento_com_mao_tentado, Lancamento_com_mao_acertado
) VALUES
('P001', '44444444444', 1, 1, 25, 22, 5, 3, 4, 2, 20, 18, 6, 3, 2, 1, 8, 6, 3, 2, 28.50, 33.40, 0.85,
 40.5, 90, 85, 2, 1, 3, 2, 1, 1, 5, 4, 0, 0, 0, 0),
('P002', '55555555555', 0, 2, 30, 28, 6, 4, 3, 1, 22, 20, 4, 2, 1, 0, 10, 8, 2, 1, 27.80, 32.10, 0.90,
 38.0, 88, 80, 1, 1, 4, 3, 0, 0, 6, 5, 0, 0, 0, 0);

INSERT INTO Analisa (fk_Analista_CPF, fk_Jogador_CPF) VALUES
('11111111111', '44444444444'),
('22222222222', '55555555555');