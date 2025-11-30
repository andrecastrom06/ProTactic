import React, { useState } from 'react'; // Removido 'useEffect'
// Removidos os imports: useAuth e buscarTodosJogadores
import { salvarPremiacao } from '../services/premiacaoService';
import './NovoAtletaModal/NovoAtletaModal.css'; // Reutiliza estilos existentes

export const RegistrarPremiacaoModal = ({ onClose, onSuccess }) => {
    // Removidos os estados: jogadores, jogadorId e clubeIdLogado (que não é mais necessário aqui)
    const [nomePremio, setNomePremio] = useState('');
    const [dataPremio, setDataPremio] = useState('');
    
    const [loading, setLoading] = useState(false);

    // Removido o useEffect para carregar jogadores

    const handleSubmit = async (e) => {
        e.preventDefault();
        // Validação simplificada (apenas nome e data são necessários)
        if (!nomePremio || !dataPremio) {
            alert("Preencha o Nome do Prémio e a Data de Referência.");
            return;
        }

        setLoading(true);
        // Removido o objeto payload com jogadorId

        try {
            // Chamada ao serviço com o novo formato (apenas nome e data)
            await salvarPremiacao(nomePremio, dataPremio); 
            
            alert("Premiação registrada com sucesso! O jogador do mês foi atribuído automaticamente.");
            onSuccess();
            onClose();
            // Limpar estados após sucesso
            setNomePremio('');
            setDataPremio('');
            
        } catch (err) {
            // O erro agora pode conter a mensagem do backend, ex: "Não foi possível encontrar um jogador..."
            alert("Erro ao salvar: " + err.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal-content" onClick={e => e.stopPropagation()}>
                <div className="modal-header">
                    <h2>Registrar Jogador do Mês (Premiação Interna)</h2>
                    <button onClick={onClose} className="modal-close-btn">&times;</button>
                </div>
                
                <form className="modal-form" onSubmit={handleSubmit}>
                    {/* Removido o campo 'Jogador Vencedor' */}
                    
                    <div className="form-group">
                        <label>Nome do Prémio *</label>
                        <input 
                            type="text" 
                            value={nomePremio} 
                            onChange={e => setNomePremio(e.target.value)} 
                            placeholder="Ex: Jogador de Janeiro"
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label>Data de Referência (Mês/Ano) *</label>
                        <input 
                            type="date" 
                            value={dataPremio} 
                            onChange={e => setDataPremio(e.target.value)} 
                            required
                        />
                        <small style={{ marginTop: '5px', display: 'block' }}>
                            A premiação será atribuída ao jogador com a melhor nota de partida registrada no mês e ano desta data.
                        </small>
                    </div>

                    <div className="modal-actions">
                        <button type="button" className="btn-cancel" onClick={onClose}>Cancelar</button>
                        <button type="submit" className="btn-submit" disabled={loading}>
                            {loading ? 'Salvando...' : 'Salvar Premiação'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};