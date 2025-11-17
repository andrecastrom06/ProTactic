package dev.com.protactic.dominio.principal.cadastroAtleta;

import dev.com.protactic.dominio.principal.*; 
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.time.LocalDate;

public class CadastroDeAtletaService {

    private final JogadorRepository jogadorRepo;
    private final ClubeRepository clubeRepo;
    private final ContratoRepository contratoRepo; 

    public CadastroDeAtletaService(JogadorRepository jogadorRepo, ClubeRepository clubeRepo, ContratoRepository contratoRepo) {
        this.jogadorRepo = jogadorRepo;
        this.clubeRepo = clubeRepo;
        this.contratoRepo = contratoRepo;
    }

    // --- INÍCIO DA CORREÇÃO 1 ---
    public record DadosNovoAtleta(
        String nomeCompleto,
        String posicao,
        int idade,
        LocalDate validadeDoContrato,
        String situacaoContratual, 
        Integer clubeId
    ) {}

    public Jogador cadastrarNovoAtleta(DadosNovoAtleta formulario) throws Exception {
        Objects.requireNonNull(formulario, "Formulário de cadastro não pode ser nulo.");
        
        Contrato novoContrato = new Contrato();
        novoContrato.setClubeId(formulario.clubeId());
        novoContrato.setStatus(formulario.situacaoContratual());
        novoContrato.setDuracaoMeses(36); 
        novoContrato.setSalario(0); 
        
        contratoRepo.salvar(novoContrato); 

        Jogador novoJogador = new Jogador();
        novoJogador.setNome(formulario.nomeCompleto());
        novoJogador.setPosicao(formulario.posicao());
        novoJogador.setIdade(formulario.idade());
        
        novoJogador.setContratoId(novoContrato.getId());
        novoJogador.setClubeId(formulario.clubeId());
        
        novoJogador.setCompeticaoId(1); 
        novoJogador.setContratoAtivo(true);
        novoJogador.setSaudavel(true);
        novoJogador.setGrauLesao(-1);
        novoJogador.setStatus("Disponível");
        novoJogador.setChegadaNoClube(LocalDate.now()); 

        jogadorRepo.salvar(novoJogador); 

        return novoJogador;
    }
    // --- FIM DA CORREÇÃO 1 ---

    // --- INÍCIO DA CORREÇÃO 2 ---
    public boolean contratarPorId(Integer clubeId, Integer jogadorId, Date data) throws Exception {
        Objects.requireNonNull(clubeId, "O ID do Clube não pode ser nulo.");
        Objects.requireNonNull(jogadorId, "O ID do Jogador não pode ser nulo.");
        Objects.requireNonNull(data, "A Data da contratação não pode ser nula.");

        Clube clubeDestino = clubeRepo.buscarPorId(clubeId);
        if (clubeDestino == null) {
            throw new Exception("Clube de destino não encontrado: " + clubeId);
        }

        Jogador jogador = jogadorRepo.buscarPorId(jogadorId);
        if (jogador == null) {
            throw new Exception("Jogador não encontrado: " + jogadorId);
        }

        return this.contratar(clubeDestino, jogador, data);
    }
    // --- FIM DA CORREÇÃO 2 ---

    private boolean estaDentroDaJanela(Date data) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        int mes = cal.get(Calendar.MONTH) + 1;
        return (mes >= 6 && mes <= 8) || (mes == 12 || mes == 1 || mes == 2);
    }

    
    public boolean contratar(Clube clubeDestino, Jogador jogador, Date data) {
        boolean janelaAberta = estaDentroDaJanela(data);
        Integer contratoAtualId = jogador.getContratoId();
        
        if (contratoAtualId != null) {
            Contrato contratoAtual = contratoRepo.buscarPorId(contratoAtualId); 
            
            if (contratoAtual != null && !contratoAtual.isExpirado()) {
                if (!janelaAberta) {
                    return false; 
                }
            }
        }
        
        Contrato novoContrato = new Contrato(clubeDestino.getId());
        contratoRepo.salvar(novoContrato); 

        jogador.setContratoId(novoContrato.getId());
        jogador.setClubeId(clubeDestino.getId());
        clubeDestino.adicionarJogadorId(jogador.getId());

        jogadorRepo.salvar(jogador);
        clubeRepo.salvar(clubeDestino);

        return true;
    }
}