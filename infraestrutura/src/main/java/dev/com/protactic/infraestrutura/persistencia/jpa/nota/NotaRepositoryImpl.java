package dev.com.protactic.infraestrutura.persistencia.jpa.nota;

import dev.com.protactic.dominio.principal.feature_09_atribuicao_notas.entidade.Nota;
import dev.com.protactic.dominio.principal.feature_09_atribuicao_notas.repositorio.NotaRepository;
import dev.com.protactic.infraestrutura.persistencia.jpa.JpaMapeador;
import dev.com.protactic.aplicacao.principal.nota.NotaRepositorioAplicacao;
import dev.com.protactic.aplicacao.principal.nota.NotaResumo;

import org.springframework.stereotype.Component;
import java.util.List; 
import java.util.Objects;
import java.util.Optional;
import java.util.Date;

@Component
public class NotaRepositoryImpl implements NotaRepository, NotaRepositorioAplicacao {

    private final NotaRepositorySpringData repositoryJPA;
    private final JpaMapeador mapeador;

    public NotaRepositoryImpl(NotaRepositorySpringData repositoryJPA, JpaMapeador mapeador) {
        this.repositoryJPA = repositoryJPA;
        this.mapeador = mapeador;
    }

    
    @Override
    public Optional<Nota> buscar(String jogoId, String jogadorId) {
        NotaPK pk = new NotaPK(jogoId, jogadorId);
        return repositoryJPA.findById(pk)
                .map(jpa -> mapeador.map(jpa, Nota.class));
    }

    @Override
    public void salvar(Nota nota) {
        Objects.requireNonNull(nota, "A Nota a ser salva não pode ser nula.");
        NotaJPA notaJPA = mapeador.map(nota, NotaJPA.class);
        Objects.requireNonNull(notaJPA, "O resultado do mapeamento de Nota para JPA não pode ser nulo.");
        repositoryJPA.save(notaJPA);
    }

    @Override
    public void limpar() {
        repositoryJPA.deleteAll();
    }


    @Override
    public void registrarParticipacao(String jogoId, String jogadorId, boolean atuou) {
        throw new UnsupportedOperationException("Implementação de 'registrarParticipacao' pendente.");
    }
    @Override
    public boolean atuouNoJogo(String jogoId, String jogadorId) {
        System.err.println("AVISO: 'atuouNoJogo' está a retornar 'true' por padrão. Implementação pendente.");
        return true; 
    }
    @Override
    public void registrarJogadorNoElenco(String jogadorId) {
        throw new UnsupportedOperationException("Implementação de 'registrarJogadorNoElenco' pendente.");
    }
    @Override
    public boolean jogadorExisteNoElenco(String jogadorId) {
        throw new UnsupportedOperationException("Implementação de 'jogadorExisteNoElenco' pendente.");
    }


    @Override
    public List<NotaResumo> pesquisarResumosPorJogo(String jogoId) {

        return repositoryJPA.findByJogoId(jogoId);
    }

    @Override
    public List<NotaResumo> pesquisarResumosPorJogador(String jogadorId) {
        return repositoryJPA.findByJogadorId(jogadorId);
    }

    @Override
    public Optional<Integer> encontrarJogadorComMelhorNotaNoMes(Date data) {
        // Chama o método customizado do Spring Data JPA.
        return repositoryJPA.findJogadorComMelhorNotaNoMes(data);
    }
    

}