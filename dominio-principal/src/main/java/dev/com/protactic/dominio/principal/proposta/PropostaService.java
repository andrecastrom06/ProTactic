package dev.com.protactic.dominio.principal.proposta;

import java.util.Date;
import java.util.Objects; // <-- 1. Adiciona import
import dev.com.protactic.dominio.principal.Clube;
import dev.com.protactic.dominio.principal.Contrato;
import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.Proposta;
import dev.com.protactic.dominio.principal.cadastroAtleta.ClubeRepository; // <-- 2. Adiciona import
import dev.com.protactic.dominio.principal.cadastroAtleta.ContratoRepository;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository; // <-- 3. Adiciona import
import dev.com.protactic.dominio.principal.cadastroAtleta.CadastroDeAtletaService; // <-- 4. Adiciona import
import dev.com.protactic.dominio.principal.dispensa.DispensaService; // <-- 5. Adiciona import

// Sem @Service, como você mencionou
public class PropostaService {
    
    // --- (INÍCIO DAS MUDANÇAS NO CONSTRUTOR) ---
    // Repositórios e Serviços necessários
    private final PropostaRepository propostaRepo;
    private final ContratoRepository contratoRepo;
    private final JogadorRepository jogadorRepo; // (Nova dependência)
    private final ClubeRepository clubeRepo;     // (Nova dependência)
    private final DispensaService dispensaService; // (Nova dependência)
    private final CadastroDeAtletaService cadastroDeAtletaService; // (Nova dependência)

    /**
     * Construtor atualizado para orquestrar a transferência completa.
     */
    public PropostaService(PropostaRepository propostaRepo,
                           ContratoRepository contratoRepo,
                           JogadorRepository jogadorRepo,
                           ClubeRepository clubeRepo,
                           DispensaService dispensaService,
                           CadastroDeAtletaService cadastroDeAtletaService) {
        this.propostaRepo = propostaRepo;
        this.contratoRepo = contratoRepo;
        this.jogadorRepo = jogadorRepo;
        this.clubeRepo = clubeRepo;
        this.dispensaService = dispensaService;
        this.cadastroDeAtletaService = cadastroDeAtletaService;
    }
    // --- (FIM DAS MUDANÇAS NO CONSTRUTOR) ---

    
    /**
     * Método de CRIAR (já existia)
     */
    public Proposta criarProposta(Jogador jogador, Clube clubePropositor, double valor, Date data) throws Exception {
        // ... (Lógica existente, não muda)
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
        Proposta proposta = new Proposta(
            clubePropositor.getId(), clubeAtualId, jogador.getId(), valor, data
        );
        return propostaRepo.saveProposta(proposta);
    }

    private boolean estaDentroDaJanelaDeTransferencia(Date data) {
        // ... (Lógica existente, não muda)
        @SuppressWarnings("deprecation")
        int mes = data.getMonth() + 1; 
        return (mes == 1 || mes == 2 || mes == 6 || mes == 7 || mes == 11 || mes == 12);
    }


    // --- (INÍCIO DAS NOVAS FUNCIONALIDADES) ---

    /**
     * Implementa a história "Aceitar proposta".
     * Esta é a lógica de negócio mais complexa, pois orquestra a transferência.
     */
    public void aceitarProposta(Integer propostaId) throws Exception {
        // 1. Encontrar os dados
        Proposta proposta = buscarPropostaOuLancarExcecao(propostaId);
        Jogador jogador = buscarJogadorOuLancarExcecao(proposta.getJogadorId());
        Clube clubePropositor = buscarClubeOuLancarExcecao(proposta.getPropositorId());
        
        // 2. Dispensar o jogador do clube antigo (se ele tiver um)
        if (jogador.getClubeId() != null) {
            // (Ignoramos a regra de 'lesionado' para transferências)
            dispensaService.dispensarJogador(jogador); 
        }

        // 3. Contratar o jogador pelo novo clube
        // (Usamos a data da proposta para a janela de transferência)
        boolean contratado = cadastroDeAtletaService.contratar(
            clubePropositor, 
            jogador, 
            proposta.getData() 
        );

        if (!contratado) {
            // Isto pode falhar se a data da proposta for inválida, etc.
            throw new Exception("Falha ao contratar o jogador pelo novo clube (verificar janela de transferência).");
        }

        // 4. Atualizar a proposta como "ACEITE"
        proposta.setStatus("ACEITE");
        propostaRepo.saveProposta(proposta);
    }

    /**
     * Implementa a história "Recusar proposta".
     */
    public void recusarProposta(Integer propostaId) throws Exception {
        Proposta proposta = buscarPropostaOuLancarExcecao(propostaId);
        proposta.setStatus("RECUSADA");
        propostaRepo.saveProposta(proposta);
    }

    /**
     * Implementa a história "Editar proposta" (mudar o valor).
     */
    public void editarValorProposta(Integer propostaId, double novoValor) throws Exception {
        Proposta proposta = buscarPropostaOuLancarExcecao(propostaId);
        if (!"PENDENTE".equalsIgnoreCase(proposta.getStatus())) {
            throw new Exception("Apenas propostas 'PENDENTES' podem ser editadas.");
        }
        proposta.setValor(novoValor);
        propostaRepo.saveProposta(proposta);
    }

    /**
     * Implementa a história "Excluir proposta".
     * (Requer adicionar 'deleteProposta' ao PropostaRepository)
     */
    public void excluirProposta(Integer propostaId) throws Exception {
        Proposta proposta = buscarPropostaOuLancarExcecao(propostaId);
        if (!"PENDENTE".equalsIgnoreCase(proposta.getStatus())) {
            throw new Exception("Apenas propostas 'PENDENTES' podem ser excluídas.");
        }
        // Assumindo que PropostaRepository tem este método
        // (Vamos ter de o adicionar no Passo 1.A)
        propostaRepo.deleteProposta(proposta); 
    }


    // --- Métodos auxiliares para buscar ou falhar ---
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
    // --- (FIM DAS NOVAS FUNCIONALIDADES) ---
}