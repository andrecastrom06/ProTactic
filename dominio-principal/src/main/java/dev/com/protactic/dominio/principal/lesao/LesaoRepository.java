package dev.com.protactic.dominio.principal.lesao;

import dev.com.protactic.dominio.principal.Lesao; // A sua entidade de domínio
import java.util.List;
import java.util.Optional;

/**
 * Este é um repositório CRUD padrão para a entidade Lesao.
 * Ele será usado pelo 'RegistroLesoesRepository' (que é mais complexo)
 * para de fato salvar e buscar lesões no banco de dados.
 */
public interface LesaoRepository {

    /**
     * Salva uma nova lesão ou atualiza uma existente.
     * * @param lesao A entidade Lesao (com o objeto Jogador)
     * @return A entidade Lesao salva (com o ID preenchido)
     */
    Lesao salvar(Lesao lesao);

    /**
     * Busca uma lesão específica pelo seu ID.
     * * @param id O ID da lesão
     * @return A lesão, se encontrada
     */
    Optional<Lesao> buscarPorId(Integer id);

    /**
     * Busca todas as lesões (ativas e inativas) de um jogador específico.
     * * @param jogadorId O ID do jogador
     * @return Uma lista de lesões
     */
    List<Lesao> buscarTodasPorJogadorId(Integer jogadorId);

    /**
     * (O MÉTODO MAIS IMPORTANTE)
     * Busca a lesão que está ATIVA (lesionado = true) para um jogador.
     * O 'RegistroLesoesRepository' usará isto o tempo todo.
     * * @param jogadorId O ID do jogador
     * @return A lesão ativa, se existir
     */
    Optional<Lesao> buscarAtivaPorJogadorId(Integer jogadorId);
}