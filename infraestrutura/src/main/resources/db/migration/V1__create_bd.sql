-- ===========================
-- Tabela Usuario
-- ===========================
create table if not exists Usuario (
    id int not null auto_increment,
    login varchar(25) not null unique,
    senha varchar(25) not null,
    nome varchar(100) not null,
    funcao varchar(50),  
    primary key (id)
);



-- ===========================
-- Tabela Competição
-- ===========================
create table if not exists Competicao (
    id int not null auto_increment,
    nome varchar(100) not null,
    data_inicio date,
    data_fim date,
    primary key (id)
);

-- ===========================
-- Tabela Contrato
-- (UNIFICADA - 'id_clube' adicionado diretamente)
-- ===========================
create table if not exists Contrato (
    id int not null auto_increment,
    duracao_meses int,
    salario decimal(12,2),
    status varchar(50),
    id_clube INT, -- Coluna movida de ALTER para cá
    primary key (id)
);

-- ===========================
-- Tabela Jogador
-- (UNIFICADA - todas as colunas do BDD movidas para cá)
-- ===========================
create table if not exists Jogador (
    id int not null auto_increment,
    id_contrato int unique,
    id_clube int,  -- FK adicionada após a criação do Clube
    id_competicao int not null,
    nome varchar(100) not null,
    idade int,
    posicao varchar(50),
    perna varchar(20),
    nota decimal(3,1),
    jogos int,
    gols int,
    assistencias int,
    
    -- Colunas movidas do ALTER TABLE para cá (baseado nos BDDs)
    status VARCHAR(50),
    minutagem VARCHAR(50),
    chegada_no_clube DATE,
    capitao BOOLEAN DEFAULT FALSE,
    grau_lesao INT DEFAULT -1,
    contrato_ativo BOOLEAN DEFAULT FALSE,
    saudavel BOOLEAN DEFAULT TRUE,
    desvio_padrao DECIMAL(10, 5) DEFAULT 0.0,
    
    primary key (id),
    foreign key (id_contrato) references Contrato(id),
    foreign key (id_competicao) references Competicao(id)
    -- A FK para 'id_clube' é adicionada abaixo
);


create table if not exists Clube (
    id int not null auto_increment,
    id_treinador int,
    id_analista int,
    id_preparador int,
    id_competicao int not null,
    nome varchar(100),
    cidade_estado varchar(100),
    estadio varchar(100),
    capitao int,
    primary key (id),
    foreign key (id_treinador) references Usuario(id),
    foreign key (id_analista) references Usuario(id),  
    foreign key (id_preparador) references Usuario(id),
    foreign key (capitao) references Jogador(id)
);

-- Adicionando a FK de Jogador -> Clube (após a tabela Clube existir)
alter table Jogador
    add constraint fk_jogador_clube
    foreign key (id_clube) references Clube(id);

-- ===========================
-- Tabela Lesão (BDD 3 - Registro de Lesões)
-- ===========================
create table if not exists Lesao (
    id int not null auto_increment,
    id_jogador int not null,
    lesionado bool,
    tempo varchar(50),
    plano varchar(255),
    grau int,
    primary key (id),
    foreign key (id_jogador) references Jogador(id)
);

-- ===========================
-- Tabela Suspensão (BDD 8 - Cartões)
-- ===========================
create table if not exists Suspensao (
    id int not null auto_increment,
    id_jogador int not null,
    suspenso bool,
    amarelo int,
    vermelho int,
    primary key (id),
    foreign key (id_jogador) references Jogador(id)
);

-- ===========================
-- Tabela Físico (BDD 2 - Carga Semanal)
-- ===========================
create table if not exists Fisico (
    id int not null auto_increment,
    id_jogador int not null,
    nome varchar(100),
    musculo varchar(100),
    intensidade varchar(50),
    descricao varchar(255),
    data_inicio date,
    data_fim date,
    status VARCHAR(50) DEFAULT 'PLANEJADO',
    primary key (id),
    foreign key (id_jogador) references Jogador(id)
);

-- ===========================
-- Tabela Partida
-- ===========================
create table if not exists Partida (
    id int not null auto_increment,
    id_clube_casa int not null,
    id_clube_visitante int not null,
    data_jogo date not null,
    hora varchar(10),
    placar_clube_casa int,
    placar_clube_visitante int,
    primary key (id),
    foreign key (id_clube_casa) references Clube(id),
    foreign key (id_clube_visitante) references Clube(id)
);

-- ===========================
-- Tabela Escalação (Usada pelo FormacaoControlador)
-- ===========================
create table if not exists Escalacao (
    id int not null auto_increment,
    id_partida int not null,
    esquema varchar(50),
    id_jogador1 int not null,
    id_jogador2 int not null,
    id_jogador3 int not null,
    id_jogador4 int not null,
    id_jogador5 int not null,
    id_jogador6 int not null,
    id_jogador7 int not null,
    id_jogador8 int not null,
    id_jogador9 int not null,
    id_jogador10 int not null,
    id_jogador11 int not null,
    primary key (id),
    foreign key (id_partida) references Partida(id),
    foreign key (id_jogador1) references Jogador(id),
    foreign key (id_jogador2) references Jogador(id),
    foreign key (id_jogador3) references Jogador(id),
    foreign key (id_jogador4) references Jogador(id),
    foreign key (id_jogador5) references Jogador(id),
    foreign key (id_jogador6) references Jogador(id),
    foreign key (id_jogador7) references Jogador(id),
    foreign key (id_jogador8) references Jogador(id),
    foreign key (id_jogador9) references Jogador(id),
    foreign key (id_jogador10) references Jogador(id),
    foreign key (id_jogador11) references Jogador(id)
);

-- ===========================
-- Tabela Tático
-- ===========================
create table if not exists Tatico (
    id int not null auto_increment,
    id_jogador int not null,
    id_partida int not null,
    categoria varchar(50),
    nome varchar(100),
    descricao varchar(255),
    primary key (id),
    foreign key (id_jogador) references Jogador(id),
    foreign key (id_partida) references Partida(id)
);

-- ===========================
-- Tabela Proposta (BDD 5)
-- (UNIFICADA - 'data' adicionada diretamente)
-- ===========================
create table if not exists Proposta (
    id int not null auto_increment,
    id_propositor int not null,
    id_receptor int,
    id_jogador int not null,
    status varchar(50),
    valor decimal(10,2),
    data DATETIME, -- Coluna movida de ALTER para cá
    primary key (id),
    foreign key (id_propositor) references Clube(id),
    foreign key (id_receptor) references Clube(id),
    foreign key (id_jogador) references Jogador(id)
);

-- ===========================
-- Tabela Premiação (BDD 11)
-- ===========================
create table if not exists Premiacao (
    id int not null auto_increment,
    id_jogador int not null,
    nome varchar(100) not null,
    data_premiacao date,
    primary key (id),
    foreign key (id_jogador) references Jogador(id)
);

-- ===========================
-- Tabela de Junção (Clube <-> Jogador IDs)
-- ===========================
CREATE TABLE IF NOT EXISTS clube_jogador_ids (
    clube_id INT NOT NULL,
    jogador_ids INT,
    FOREIGN KEY (clube_id) REFERENCES Clube(id)
);

-- ===========================
-- Tabela Nota (BDD 9 - Atribuição de Notas)
-- ===========================
CREATE TABLE IF NOT EXISTS Nota (
    jogo_id VARCHAR(255) NOT NULL,
    jogador_id VARCHAR(255) NOT NULL,
    nota DECIMAL(3, 1),
    observacao TEXT,
    PRIMARY KEY (jogo_id, jogador_id)
);

-- ===========================
-- Tabela RegistroCartao (BDD 8 - Cartões)
-- ===========================
CREATE TABLE IF NOT EXISTS registro_cartao (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_jogador INT NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    FOREIGN KEY (id_jogador) REFERENCES Jogador(id) 
        ON DELETE CASCADE
);

-- ===========================
-- Tabela Escalacao Simples (BDD 4 - Esquema Tático)
-- ===========================
CREATE TABLE IF NOT EXISTS escalacao_simples (
    id INT AUTO_INCREMENT PRIMARY KEY,
    jogo_data VARCHAR(255) NOT NULL,
    nome_jogador VARCHAR(255) NOT NULL
);

-- ===========================
-- Tabela InscricaoAtleta (BDD 6 - Inscrição)
-- ===========================
CREATE TABLE IF NOT EXISTS inscricao_atleta (
    atleta VARCHAR(255) NOT NULL,
    competicao VARCHAR(255) NOT NULL,
    elegivel_para_jogos BOOLEAN DEFAULT FALSE,
    inscrito BOOLEAN DEFAULT FALSE,
    mensagem_erro VARCHAR(255),
    PRIMARY KEY (atleta, competicao)
);

-- ===========================
-- Tabela SessaoTreino (BDD 10 - Treino Tático)
-- ===========================
CREATE TABLE IF NOT EXISTS sessao_treino (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    id_partida INT NOT NULL,
    FOREIGN KEY (id_partida) REFERENCES Partida(id)
);

-- ===========================
-- Tabela SessaoTreino Convocados (BDD 10)
-- ===========================
CREATE TABLE IF NOT EXISTS sessao_treino_convocados (
    sessao_treino_id INT NOT NULL,
    jogador_id INT NOT NULL,
    FOREIGN KEY (sessao_treino_id) REFERENCES sessao_treino(id),
    FOREIGN KEY (jogador_id) REFERENCES Jogador(id),
    PRIMARY KEY (sessao_treino_id, jogador_id)
);