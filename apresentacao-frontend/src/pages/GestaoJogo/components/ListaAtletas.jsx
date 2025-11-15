import { useDroppable } from '@dnd-kit/core';
import { JogadorDisponivel } from './JogadorDisponivel';
import './ListaAtletas.css';

const DroppableList = ({ id, title, atletas, type }) => {
    const { setNodeRef } = useDroppable({
        id: id,
    });

    return (
        <div className="lista-atletas-container">
            <h3>{title}</h3>
            <div ref={setNodeRef} className="lista-atletas-box">
                {atletas.map((atleta) => (
                    <JogadorDisponivel key={atleta.id} atleta={atleta} type={type} />
                ))}
                {/* Se a lista estiver vazia, mostre um placeholder */}
                {atletas.length === 0 && (
                    <div className="lista-vazia-placeholder">
                        Arraste um jogador para cá
                    </div>
                )}
            </div>
        </div>
    );
};


export const ListaAtletas = ({ disponiveis, reservas }) => {
    return (
        <>
            <DroppableList
                id="DISPONIVEIS_LIST"
                title="Atletas Disponíveis"
                atletas={disponiveis}
                type="DISPONIVEL"
            />
            
            <DroppableList
                id="RESERVAS_LIST"
                title="Reservas"
                atletas={reservas}
                type="RESERVA"
            />
        </>
    );
};