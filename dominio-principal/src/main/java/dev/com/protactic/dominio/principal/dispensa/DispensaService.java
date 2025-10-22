package dev.com.protactic.dominio.principal.dispensa;

// MUDANÇA: Importar todas as entidades e repositórios necessários
import dev.com.protactic.dominio.principal.Clube;
import dev.com.protactic.dominio.principal.Contrato;
import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.cadastroAtleta.IClubeRepository;
import dev.com.protactic.dominio.principal.cadastroAtleta.IContratoRepository;
import dev.com.protactic.dominio.principal.cadastroAtleta.IJogadorRepository;

public class DispensaService {
    
    // MUDANÇA: O serviço precisa dos 3 repositórios para orquestrar a dispensa
    private final IContratoRepository contratoRepo;
    private final IJogadorRepository jogadorRepo;
    private final IClubeRepository clubeRepo;

    // MUDANÇA: Construtor atualizado para injeção de dependência das interfaces
    public DispensaService(IContratoRepository contratoRepo, IJogadorRepository jogadorRepo, IClubeRepository clubeRepo) {
        this.contratoRepo = contratoRepo;
        this.jogadorRepo = jogadorRepo;
        this.clubeRepo = clubeRepo;
    }

    /**
     * Orquestra a dispensa de um jogador, rescindindo seu contrato
     * e removendo-o do clube.
     * @param jogador O agregado Jogador a ser dispensado.
     * @throws Exception Se as regras de negócio forem violadas.
     */
    public void dispensarJogador(Jogador jogador) throws Exception {
        if (jogador == null) {
            throw new Exception("Jogador não pode ser nulo.");
        }
        
        // MUDANÇA: Erro [14,39] corrigido. Usar getContratoId()
        if (jogador.getContratoId() == null) {
            throw new Exception("Jogador não possui contrato ativo.");
        }

        if (!jogadorSaudavel(jogador)) {
            throw new Exception("Não é permitido dispensar jogadores que estão lesionados.");
        }

        // --- Início da Orquestração ---

        // 1. Buscar e modificar o Agregado Contrato
        // MUDANÇA: Erros [22,16] e [27,48] corrigidos.
        Contrato contrato = contratoRepo.buscarPorId(jogador.getContratoId());
        if (contrato == null) {
            throw new Exception("Contrato não encontrado no repositório.");
        }
        contrato.setStatus("RESCINDIDO");
        contratoRepo.salvar(contrato); // Salva o contrato rescindido

        // 2. Buscar e modificar o Agregado Clube (o clube antigo)
        Integer clubeAntigoId = jogador.getClubeId();
        if (clubeAntigoId != null) {
            Clube clubeAntigo = clubeRepo.buscarPorId(clubeAntigoId);
            if (clubeAntigo != null) {
                clubeAntigo.removerJogadorId(jogador.getId());
                clubeRepo.salvar(clubeAntigo);
            }
        }

        // 3. Modificar o Agregado Jogador (agora ele é um "Passe Livre")
        // MUDANÇA: Erro [25,16] corrigido.
        // Em vez de 'new Clube("Passes Livres")', setamos o ID do clube como nulo.
        jogador.setClubeId(null);
        jogador.setContratoId(null); // O contrato foi rescindido, ele não tem mais um contrato ativo.
        jogadorRepo.salvar(jogador); // Salva o jogador atualizado

        // --- Fim da Orquestração ---
    }

    /**
     * Verifica o estado de saúde do jogador.
     * Esta lógica é interna do agregado Jogador, então está correto.
     */
    private boolean jogadorSaudavel(Jogador jogador) {
        return jogador.isSaudavel();
    }
}