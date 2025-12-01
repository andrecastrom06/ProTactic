package dev.com.protactic.apresentacao; // <-- PACOTE CORRETO

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

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

import java.util.List;
import java.util.stream.Collectors;


@Component
public class BackendMapeador extends ModelMapper { 

    BackendMapeador() {

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

        this.createTypeMap(Premiacao.class, PremiacaoJPA.class)
            .addMappings(mapper -> 
                mapper.using(objetoParaIdConverter).map(Premiacao::getJogador, PremiacaoJPA::setJogadorId)
            );

        this.createTypeMap(SessaoTreino.class, SessaoTreinoJPA.class)
            .addMappings(mapper -> {
                mapper.using(objetoParaIdConverter).map(SessaoTreino::getPartida, SessaoTreinoJPA::setPartidaId);
                mapper.using(listaJogadorParaListaIdConverter).map(SessaoTreino::getConvocados, SessaoTreinoJPA::setConvocadosIds);
            });
            
        this.createTypeMap(Partida.class, PartidaJPA.class)
            .addMappings(mapper -> {
                mapper.using(objetoParaIdConverter).map(Partida::getClubeCasa, PartidaJPA::setClubeCasaId);
                mapper.using(objetoParaIdConverter).map(Partida::getClubeVisitante, PartidaJPA::setClubeVisitanteId);
            });

        this.createTypeMap(Lesao.class, LesaoJPA.class)
            .addMappings(mapper -> {
                mapper.using(objetoParaIdConverter).map(Lesao::getJogador, LesaoJPA::setJogadorId);
            });

    }

    @Override
    public <D> D map(Object source, Class<D> destinationType) {
        return source != null ? super.map(source, destinationType) : null;
    }
}