package dev.com.protactic.mocks;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import dev.com.protactic.dominio.principal.feature_03_registro_lesao.repositorio.RegistroLesoesRepository;

public class RegistroLesoesMock implements RegistroLesoesRepository {

    private static class EstadoAtleta {
        boolean contratoAtivo = false;

        String statusAtleta = "Saudável";       
        String disponibilidade = "disponível";    
        String permissaoTreino = "liberada";      

        Integer lesaoGrau = null;               
        String  lesaoStatus = "nenhuma";        

        Integer planoDias = null;             
    }

    private final Map<String, EstadoAtleta> atletas = new HashMap<>();

    private EstadoAtleta st(String atletaId) {
        return atletas.computeIfAbsent(atletaId, k -> new EstadoAtleta());
    }

    @Override public void cadastrarAtleta(String atletaId) { st(atletaId); }

    @Override public void definirContratoAtivo(String atletaId, boolean ativo) { st(atletaId).contratoAtivo = ativo; }

    @Override public boolean contratoAtivo(String atletaId) { return st(atletaId).contratoAtivo; }

    @Override public void atualizarStatusAtleta(String atletaId, String status) { st(atletaId).statusAtleta = status; }

    @Override public String statusAtleta(String atletaId) { return st(atletaId).statusAtleta; }

    @Override public void atualizarDisponibilidade(String atletaId, String disponibilidade) { st(atletaId).disponibilidade = disponibilidade; }

    @Override public String disponibilidadeAtleta(String atletaId) { return st(atletaId).disponibilidade; }

    @Override public void atualizarPermissaoTreino(String atletaId, String permissao) { st(atletaId).permissaoTreino = permissao; }

    @Override public String permissaoTreino(String atletaId) { return st(atletaId).permissaoTreino; }

    @Override public Optional<Integer> grauLesaoAtiva(String atletaId) { return Optional.ofNullable(st(atletaId).lesaoGrau); }

    @Override public String lesaoStatus(String atletaId) { return st(atletaId).lesaoStatus; }

    @Override public void salvarLesaoAtiva(String atletaId, int grau) {
        var s = st(atletaId);
        s.lesaoGrau = grau;
        s.lesaoStatus = "ativa";
    }

    @Override public void encerrarLesaoAtiva(String atletaId) {
        var s = st(atletaId);
        s.lesaoStatus = s.lesaoGrau != null ? "encerrada" : "nenhuma";
        s.lesaoGrau = null;
    }

    @Override public Optional<Integer> planoDias(String atletaId) { return Optional.ofNullable(st(atletaId).planoDias); }

    @Override public void salvarPlanoDias(String atletaId, int dias) { st(atletaId).planoDias = dias; }

    @Override public void limpar() { atletas.clear(); }
}