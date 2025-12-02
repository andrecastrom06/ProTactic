package dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.servico;

import java.util.Date;
import java.util.Objects;

import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Clube;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Jogador;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio.ClubeRepository;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio.JogadorRepository;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.servico.CadastroDeAtletaService;
import dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.entidade.Contrato;
import dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.entidade.Proposta;
import dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.repositorio.ContratoRepository;
import dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.repositorio.PropostaRepository;
import dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.template.GeradorProposta;
import dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.template.GeradorPropostaBase;
import dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.template.GeradorPropostaEstrela;
import dev.com.protactic.dominio.principal.feature_12_dispensa_rescisao.servico.DispensaService;
import dev.com.protactic.dominio.principal.feature_12_dispensa_rescisao.servico.IDispensaService; 

public class PropostaService {
    
    private final PropostaRepository propostaRepo;
    private final ContratoRepository contratoRepo;
    private final JogadorRepository jogadorRepo; 
    private final ClubeRepository clubeRepo;     
    private final IDispensaService dispensaService; 
    private final CadastroDeAtletaService cadastroDeAtletaService; 

    public PropostaService(PropostaRepository propostaRepo,
                           ContratoRepository contratoRepo,
                           JogadorRepository jogadorRepo,
                           ClubeRepository clubeRepo,
                           IDispensaService dispensaService,
                           CadastroDeAtletaService cadastroDeAtletaService) {
        this.propostaRepo = propostaRepo;
        this.contratoRepo = contratoRepo;
        this.jogadorRepo = jogadorRepo;
        this.clubeRepo = clubeRepo;
        this.dispensaService = dispensaService;
        this.cadastroDeAtletaService = cadastroDeAtletaService;
    }
    
    public record DadosNovaProposta(
        Integer jogadorId,
        Integer clubePropositorId,
        double valor,
        Date data
    ) {}

    public Proposta criarPropostaPorIds(DadosNovaProposta dados) throws Exception {
        Objects.requireNonNull(dados.jogadorId(), "ID do Jogador não pode ser nulo.");
        Objects.requireNonNull(dados.clubePropositorId(), "ID do Clube Propositor não pode ser nulo.");
        Objects.requireNonNull(dados.data(), "Data da proposta não pode ser nula.");

        Jogador jogador = buscarJogadorOuLancarExcecao(dados.jogadorId());
        Clube clubePropositor = buscarClubeOuLancarExcecao(dados.clubePropositorId());

        return this.criarProposta(
            jogador,
            clubePropositor,
            dados.valor(),
            dados.data()
        );
    }
    
    public Proposta criarProposta(Jogador jogador, Clube clubePropositor, double valor, Date data) throws Exception {
        Integer clubeAtualId = null;
        Integer contratoId = jogador.getContratoId();
        
        if (contratoId != null) {
            Contrato contrato = contratoRepo.buscarPorId(contratoId);
            if (contrato != null && "ATIVO".equalsIgnoreCase(contrato.getStatus())) {
                clubeAtualId = jogador.getClubeId();
                if (clubeAtualId != null && clubeAtualId.equals(clubePropositor.getId())) {
                    throw new Exception("Jogador já possui contrato ativo com o clube.");
                }
                if (data == null || !estaDentroDaJanelaDeTransferencia(data)) {
                    throw new Exception("Jogador não pode ser contratado fora do prazo de transferência.");
                }
            }
        }
        GeradorProposta gerador;
        if (jogador.getIdade() < 20) {
            gerador = new GeradorPropostaBase();
        } else {
            gerador = new GeradorPropostaEstrela();
        }

        Proposta proposta = gerador.gerarProposta(jogador, clubePropositor, clubeAtualId, valor, data);

        return propostaRepo.saveProposta(proposta);
    }

    private boolean estaDentroDaJanelaDeTransferencia(Date data) {
        @SuppressWarnings("deprecation")
        int mes = data.getMonth() + 1; 
        return (mes == 1 || mes == 2 || mes == 6 || mes == 7 || mes == 11 || mes == 12);
    }

    public void aceitarProposta(Integer propostaId) throws Exception {
        Proposta proposta = buscarPropostaOuLancarExcecao(propostaId);
        Jogador jogador = buscarJogadorOuLancarExcecao(proposta.getJogadorId());
        Clube clubePropositor = buscarClubeOuLancarExcecao(proposta.getPropositorId());
        
        if (jogador.getClubeId() != null) {
            dispensaService.dispensarJogador(jogador); 
        }

        boolean contratado = cadastroDeAtletaService.contratar(
            clubePropositor, 
            jogador, 
            proposta.getData() 
        );

        if (!contratado) {
            throw new Exception("Falha ao contratar o jogador pelo novo clube (verificar janela de transferência).");
        }

        Jogador jogadorContratado = jogadorRepo.buscarPorId(jogador.getId());
        
        if (jogadorContratado.getContratoId() != null) {
            Contrato novoContrato = contratoRepo.buscarPorId(jogadorContratado.getContratoId());
            
            if (novoContrato != null) {
                novoContrato.setSalario(proposta.getValor());
                contratoRepo.salvar(novoContrato);
            }
        }

        proposta.setStatus("ACEITA");
        propostaRepo.saveProposta(proposta);
    }

    public void recusarProposta(Integer propostaId) throws Exception {
        Proposta proposta = buscarPropostaOuLancarExcecao(propostaId);
        proposta.setStatus("RECUSADA");
        propostaRepo.saveProposta(proposta);
    }

    public void editarValorProposta(Integer propostaId, double novoValor) throws Exception {
        Proposta proposta = buscarPropostaOuLancarExcecao(propostaId);
        if (!"PENDENTE".equalsIgnoreCase(proposta.getStatus())) {
            throw new Exception("Apenas propostas 'PENDENTES' podem ser editadas.");
        }
        proposta.setValor(novoValor);
        propostaRepo.saveProposta(proposta);
    }

    public void excluirProposta(Integer propostaId) throws Exception {
        Proposta proposta = buscarPropostaOuLancarExcecao(propostaId);
        if (!"PENDENTE".equalsIgnoreCase(proposta.getStatus())) {
            throw new Exception("Apenas propostas 'PENDENTES' podem ser excluídas.");
        }
        
        propostaRepo.deleteProposta(proposta); 
    }

    private Proposta buscarPropostaOuLancarExcecao(Integer propostaId) throws Exception {
        Objects.requireNonNull(propostaId, "O ID da Proposta não pode ser nulo.");
        Proposta proposta = propostaRepo.findPropostaById(propostaId);
        if (proposta == null) {
            throw new Exception("Proposta com ID " + propostaId + " não encontrada.");
        }
        return proposta;
    }

    private Jogador buscarJogadorOuLancarExcecao(Integer jogadorId) throws Exception {
        Objects.requireNonNull(jogadorId, "O ID do Jogador na proposta não pode ser nulo.");
        Jogador jogador = jogadorRepo.buscarPorId(jogadorId);
        if (jogador == null) {
            throw new Exception("Jogador com ID " + jogadorId + " não encontrado.");
        }
        return jogador;
    }

    private Clube buscarClubeOuLancarExcecao(Integer clubeId) throws Exception {
        Objects.requireNonNull(clubeId, "O ID do Clube na proposta não pode ser nulo.");
        Clube clube = clubeRepo.buscarPorId(clubeId);
        if (clube == null) {
            throw new Exception("Clube com ID " + clubeId + " não encontrado.");
        }
        return clube;
    }
}