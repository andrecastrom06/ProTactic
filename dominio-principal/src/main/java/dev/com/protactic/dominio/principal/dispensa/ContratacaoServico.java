package dev.com.protactic.dominio.principal.dispensa;

// MUDANÇA: Importar as classes necessárias
import dev.com.protactic.dominio.principal.Clube;
import dev.com.protactic.dominio.principal.Contrato;
import dev.com.protactic.dominio.principal.Jogador;

// MUDANÇA: Importar os repositórios que este serviço irá orquestrar
import dev.com.protactic.dominio.principal.cadastroAtleta.IClubeRepository;
import dev.com.protactic.dominio.principal.cadastroAtleta.IJogadorRepository;
import dev.com.protactic.dominio.principal.cadastroAtleta.IContratoRepository; // O repositório que criamos

public class ContratacaoServico {

    // MUDANÇA: Adicionar os repositórios como dependências
    private final IClubeRepository clubeRepo;
    private final IJogadorRepository jogadorRepo;
    private final IContratoRepository contratoRepo;

    // MUDANÇA: Adicionar o construtor para injeção de dependência
    public ContratacaoServico(IClubeRepository clubeRepo, IJogadorRepository jogadorRepo, IContratoRepository contratoRepo) {
        this.clubeRepo = clubeRepo;
        this.jogadorRepo = jogadorRepo;
        this.contratoRepo = contratoRepo;
    }

    /**
     * Tenta registrar um novo atleta (free agent) em um clube, seguindo as regras de negócio.
     * @param clubeDestino O clube que está tentando contratar.
     * @param jogador O atleta a ser contratado.
     * @param janelaDeTransferenciaAberta Se a janela de transferências está aberta.
     * @return {@code true} se a contratação for bem-sucedida, {@code false} caso contrário.
     */
    public boolean registrarAtleta(Clube clubeDestino, Jogador jogador, boolean janelaDeTransferenciaAberta) {
        
        // MUDANÇA: Lógica atualizada para usar IDs e orquestração de serviço
        
        // Caso 1: Jogador está sem clube (free agent)
        // Erros [17,20] e [23,20] corrigidos: `getClube()` -> `getClubeId()`
        if (jogador.getClubeId() == null) {
            
            // 1. Criar o novo Agregado Contrato (usando o ID do clube)
            Contrato novoContrato = new Contrato(clubeDestino.getId());
            
            // 2. Salvar o novo Agregado (Contrato) para ele ter um ID gerado
            contratoRepo.salvar(novoContrato);

            // 3. Atualizar o Agregado Clube
            // Erro [18,25] corrigido: `adicionarJogador(Jogador)` -> `adicionarJogadorId(Integer)`
            clubeDestino.adicionarJogadorId(jogador.getId());
            
            // 4. Atualizar o Agregado Jogador com os novos IDs
            // Erro [19,46] corrigido: lógica de `setContrato`
            jogador.setContratoId(novoContrato.getId());
            jogador.setClubeId(clubeDestino.getId()); // Essencial: ligar o jogador ao novo clube

            // 5. Salvar os Agregados modificados (Clube e Jogador)
            clubeRepo.salvar(clubeDestino);
            jogadorRepo.salvar(jogador);
            
            return true;
        }

        // Caso 2: Jogador já tem clube
        // Erro [23,20] corrigido: `getClube()` -> `getClubeId()`
        if (jogador.getClubeId() != null && !janelaDeTransferenciaAberta) {
            System.out.println("Falha ao contratar " + jogador.getNome() + ": A janela de transferências está fechada.");
            return false;
        }
        
        // Caso 3: Jogador tem clube E a janela está aberta.
        // A lógica do seu `CadastroDeAtletaService` é que lida com esse caso.
        // Este serviço (ContratacaoServico) parece ser apenas para free agents.
        // Mantendo a lógica original de retornar false.
        return false;
    }
}