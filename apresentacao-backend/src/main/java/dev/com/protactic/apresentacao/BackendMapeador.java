package dev.com.protactic.apresentacao; // <-- PACOTE CORRETO

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

// --- Imports que você vai precisar ---
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.Premiacao;
import dev.com.protactic.dominio.principal.Partida;
import dev.com.protactic.dominio.principal.Clube;
import dev.com.protactic.dominio.principal.SessaoTreino;
import dev.com.protactic.dominio.principal.Lesao;
import dev.com.protactic.infraestrutura.persistencia.jpa.lesao.LesaoJPA;
import dev.com.protactic.infraestrutura.persistencia.jpa.partida.PartidaJPA;
import dev.com.protactic.infraestrutura.persistencia.jpa.premiacao.PremiacaoJPA;
import dev.com.protactic.infraestrutura.persistencia.jpa.sessaotreino.SessaoTreinoJPA;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class BackendMapeador extends ModelMapper { // Ele é o ModelMapper

    BackendMapeador() {
        // --- COPIE TODA A LÓGICA DE CONFIGURAÇÃO PARA CÁ ---

        // --- CONVERSOR UNIVERSAL DE OBJETO -> ID ---
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

        // --- CONVERSOR: LISTA DE OBJETOS -> LISTA DE IDs ---
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

        // --- MAPEAMENTO DO 'PREMIACAO' ---
        this.createTypeMap(Premiacao.class, PremiacaoJPA.class)
            .addMappings(mapper -> 
                mapper.using(objetoParaIdConverter).map(Premiacao::getJogador, PremiacaoJPA::setJogadorId)
            );

        // --- MAPEAMENTO DO 'SESSAOTREINO' ---
        this.createTypeMap(SessaoTreino.class, SessaoTreinoJPA.class)
            .addMappings(mapper -> {
                mapper.using(objetoParaIdConverter).map(SessaoTreino::getPartida, SessaoTreinoJPA::setPartidaId);
                mapper.using(listaJogadorParaListaIdConverter).map(SessaoTreino::getConvocados, SessaoTreinoJPA::setConvocadosIds);
            });
            
        // --- MAPEAMENTO DO 'PARTIDA' ---
        this.createTypeMap(Partida.class, PartidaJPA.class)
            .addMappings(mapper -> {
                mapper.using(objetoParaIdConverter).map(Partida::getClubeCasa, PartidaJPA::setClubeCasaId);
                mapper.using(objetoParaIdConverter).map(Partida::getClubeVisitante, PartidaJPA::setClubeVisitanteId);
            });

        // --- MAPEAMENTO: 'Lesao' -> 'LesaoJPA' ---
        this.createTypeMap(Lesao.class, LesaoJPA.class)
            .addMappings(mapper -> {
                mapper.using(objetoParaIdConverter).map(Lesao::getJogador, LesaoJPA::setJogadorId);
            });

        // --- FIM DA LÓGICA DE CONFIGURAÇÃO ---
    }

    @Override
    public <D> D map(Object source, Class<D> destinationType) {
        // Seu método de verificação de nulo está ótimo
        return source != null ? super.map(source, destinationType) : null;
    }
}