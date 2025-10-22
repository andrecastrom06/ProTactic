package dev.com.protactic.dominio.principal.proposta;

import java.util.Date;

// MUDANÇA: Importar as entidades e os REPOSITÓRIOS
import dev.com.protactic.dominio.principal.Clube;
import dev.com.protactic.dominio.principal.Contrato;
import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.Proposta;
import dev.com.protactic.dominio.principal.cadastroAtleta.IContratoRepository;
// MUDANÇA: Assumindo que você tem uma interface IPropostaRepository
// Se o seu arquivo for PropostaRepository.java, mude o nome
// da interface abaixo e no construtor.
 


public class PropostaService {
    
    // MUDANÇA: Depender das interfaces dos repositórios
    private final PropostaRepository propostaRepo;
    private final IContratoRepository contratoRepo;
    // O IClubeRepository não é necessário se só compararmos IDs

    // MUDANÇA: Construtor atualizado para injeção
    public PropostaService(PropostaRepository propostaRepo, IContratoRepository contratoRepo) {
        this.propostaRepo = propostaRepo;
        this.contratoRepo = contratoRepo;
    }

    /**
     * Orquestra a criação de uma proposta para um jogador.
     * @param jogador O agregado Jogador (alvo da proposta)
     * @param clubePropositor O agregado Clube (que faz a proposta)
     * @param data A data da proposta
     * @return A nova Proposta salva
     * @throws Exception Se as regras de negócio forem violadas
     */
    public Proposta criarProposta(Jogador jogador, Clube clubePropositor, Date data) throws Exception {
        
        Integer clubeAtualId = null;
        
        // MUDANÇA: Erros [17,20], [17,68], [18,39] corrigidos
        // 1. Pegar o ID do contrato do jogador
        Integer contratoId = jogador.getContratoId();

        if (contratoId != null) {
            // 2. Buscar o agregado Contrato usando o repositório
            Contrato contrato = contratoRepo.buscarPorId(contratoId);

            if (contrato != null && "ATIVO".equals(contrato.getStatus())) {
                // 3. Pegar o ID do clube ATUAL do contrato
                clubeAtualId = contrato.getClubeId();

                // 4. Comparar IDs, não objetos
                if (clubeAtualId != null && clubeAtualId.equals(clubePropositor.getId())) {
                    throw new Exception("Jogador já possui contrato ativo com o clube.");
                }

                // 5. Validar a janela de transferência
                if (data == null || !estaDentroDaJanelaDeTransferencia(data)) {
                    throw new Exception("Jogador não pode ser contratado fora do prazo de transferência.");
                }
            }
        }

        // MUDANÇA: Erro [29,29] (construtor) corrigido.
        // Usamos o construtor de dados simples que criamos em Proposta.java.
        // O "clube" é o propositor. O "clubeAtualId" é o receptor.
        // (Assumindo que uma proposta não tem valor inicial, passando 0.0)
        Proposta proposta = new Proposta(
            clubePropositor.getId(), 
            clubeAtualId, 
            jogador.getId(), 
            0.0, // Valor da proposta (não estava no método original)
            data
        );
        
        // 6. Salvar o novo agregado Proposta
        // MUDANÇA: Usando o nome de método do seu CML/código
        return propostaRepo.saveProposta(proposta);
    }


    private boolean estaDentroDaJanelaDeTransferencia(Date data) {
        @SuppressWarnings("deprecation")
        // MUDANÇA: +1 estava faltando no seu getMonth() original (Date.getMonth() é 0-11)
        int mes = data.getMonth() + 1; 
        return (mes == 1 || mes == 2 || mes == 6 || mes == 7);
    }
}