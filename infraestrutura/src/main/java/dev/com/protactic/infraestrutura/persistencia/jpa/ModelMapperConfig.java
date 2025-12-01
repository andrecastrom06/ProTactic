package dev.com.protactic.infraestrutura.persistencia.jpa;

import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Clube;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Jogador;
import dev.com.protactic.dominio.principal.feature_03_registro_lesao.entidade.Lesao;
import dev.com.protactic.dominio.principal.feature_09_atribuicao_notas.entidade.Partida;
import dev.com.protactic.dominio.principal.feature_10_treino_tatico.entidade.SessaoTreino;
import dev.com.protactic.dominio.principal.feature_11_premiacao_interna.entidade.Premiacao;
import dev.com.protactic.infraestrutura.persistencia.jpa.lesao.LesaoJPA;
import dev.com.protactic.infraestrutura.persistencia.jpa.partida.PartidaJPA;
import dev.com.protactic.infraestrutura.persistencia.jpa.premiacao.PremiacaoJPA;
import dev.com.protactic.infraestrutura.persistencia.jpa.sessaotreino.SessaoTreinoJPA;

import org.modelmapper.ModelMapper;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.List;
import java.util.stream.Collectors;


public class ModelMapperConfig {


    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        Converter<Object, Integer> objetoParaIdConverter = new Converter<Object, Integer>() {
            public Integer convert(MappingContext<Object, Integer> context) {
                if (context.getSource() == null) {
                    return null;
                }
                if (context.getSource() instanceof Jogador) {
                    return ((Jogador) context.getSource()).getId();
                }
                if (context.getSource() instanceof Clube) {
                    return ((Clube) context.getSource()).getId();
                }
                if (context.getSource() instanceof Partida) {
                    return ((Partida) context.getSource()).getId();
                }
                return null;
            }
        };

        Converter<List<Jogador>, List<Integer>> listaJogadorParaListaIdConverter = new Converter<List<Jogador>, List<Integer>>() {
            public List<Integer> convert(MappingContext<List<Jogador>, List<Integer>> context) {
                if (context.getSource() == null) {
                    return null;
                }
                return context.getSource().stream()
                        .map(Jogador::getId)
                        .collect(Collectors.toList());
            }
        };

        modelMapper.createTypeMap(Premiacao.class, PremiacaoJPA.class)
            .addMappings(mapper -> 
                mapper.using(objetoParaIdConverter).map(Premiacao::getJogador, PremiacaoJPA::setJogadorId)
            );

        modelMapper.createTypeMap(SessaoTreino.class, SessaoTreinoJPA.class)
            .addMappings(mapper -> {
                mapper.using(objetoParaIdConverter).map(SessaoTreino::getPartida, SessaoTreinoJPA::setPartidaId);
                mapper.using(listaJogadorParaListaIdConverter).map(SessaoTreino::getConvocados, SessaoTreinoJPA::setConvocadosIds);
            });
            
        modelMapper.createTypeMap(Partida.class, PartidaJPA.class)
            .addMappings(mapper -> {
                mapper.using(objetoParaIdConverter).map(Partida::getClubeCasa, PartidaJPA::setClubeCasaId);
                mapper.using(objetoParaIdConverter).map(Partida::getClubeVisitante, PartidaJPA::setClubeVisitanteId);
            });

        modelMapper.createTypeMap(Lesao.class, LesaoJPA.class)
            .addMappings(mapper -> {
                mapper.using(objetoParaIdConverter).map(Lesao::getJogador, LesaoJPA::setJogadorId);
            });

        return modelMapper;
    }
}