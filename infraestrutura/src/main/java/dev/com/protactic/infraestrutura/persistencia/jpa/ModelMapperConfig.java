package dev.com.protactic.infraestrutura.persistencia.jpa;

import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.Premiacao;
import dev.com.protactic.dominio.principal.Partida;
import dev.com.protactic.dominio.principal.Clube;
import dev.com.protactic.dominio.principal.SessaoTreino;
import dev.com.protactic.infraestrutura.persistencia.jpa.lesao.LesaoJPA;
import dev.com.protactic.infraestrutura.persistencia.jpa.partida.PartidaJPA;
import dev.com.protactic.infraestrutura.persistencia.jpa.premiacao.PremiacaoJPA;
import dev.com.protactic.infraestrutura.persistencia.jpa.sessaotreino.SessaoTreinoJPA;
import dev.com.protactic.dominio.principal.Lesao; // 1. Importa o domínio Lesao

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