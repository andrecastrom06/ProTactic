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
