-- Cria o banco de dados
create database if not exists ProTactic;
use ProTactic;

-- ===========================
-- Tabela Usuario
-- ===========================
create table if not exists Usuario (
    id int not null auto_increment,
    login varchar(25) not null unique,
    senha varchar(25) not null,
    nome varchar(100) not null,
    primary key (id)
);

-- ===========================
-- Tabelas de especialização
-- ===========================
create table if not exists Treinador (
    id int not null,
    primary key (id),
    foreign key (id) references Usuario(id)
);

create table if not exists Analista (
    id int not null,
    primary key (id),
    foreign key (id) references Usuario(id)
);

create table if not exists Preparador (
    id int not null,
    primary key (id),
    foreign key (id) references Usuario(id)
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
-- ===========================
create table if not exists Contrato (
    id int not null auto_increment,
    duracao_meses int,
    salario decimal(12,2),
    status varchar(50),
    primary key (id)
);

-- ===========================
-- Tabela Jogador
-- ===========================
create table if not exists Jogador (
    id int not null auto_increment,
    id_contrato int unique,
    id_clube int,   -- ainda sem FK, pq Clube ainda não existe
    id_competicao int not null,
    nome varchar(100) not null,
    idade int,
    posicao varchar(50),
    perna varchar(20),
    nota decimal(3,1),
    jogos int,
    gols int,
    assistencias int,
    primary key (id),
    foreign key (id_contrato) references Contrato(id),
    foreign key (id_competicao) references Competicao(id)
);

-- ===========================
-- Tabela Clube
-- ===========================
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
    vice_capitao int,
    primary key (id),
    foreign key (id_treinador) references Treinador(id),
    foreign key (id_analista) references Analista(id),
    foreign key (id_preparador) references Preparador(id),
    foreign key (id_competicao) references Competicao(id),
    foreign key (capitao) references Jogador(id),
    foreign key (vice_capitao) references Jogador(id)
);

-- Agora adicionamos a FK de Jogador -> Clube
alter table Jogador
    add constraint fk_jogador_clube
    foreign key (id_clube) references Clube(id);

-- ===========================
-- Tabela Lesão
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
-- Tabela Suspensão
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
-- Tabela Físico
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
    hora time,
    placar_clube_casa int,
    placar_clube_visitante int,
    primary key (id),
    foreign key (id_clube_casa) references Clube(id),
    foreign key (id_clube_visitante) references Clube(id)
);

-- ===========================
-- Tabela Escalação
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
-- Tabela Proposta
-- ===========================
create table if not exists Proposta (
    id int not null auto_increment,
    id_propositor int not null,
    id_receptor int not null,
    id_jogador int not null,
    status varchar(50),
    valor decimal(10,2),
    primary key (id),
    foreign key (id_propositor) references Clube(id),
    foreign key (id_receptor) references Clube(id),
    foreign key (id_jogador) references Jogador(id)
);

-- ===========================
-- Tabela Premiação
-- ===========================
create table if not exists Premiacao (
    id int not null auto_increment,
    id_jogador int not null,
    nome varchar(100) not null,
    data_premiacao date,
    primary key (id),
    foreign key (id_jogador) references Jogador(id)
);
