import React from 'react';
import { useDraggable } from '@dnd-kit/core';
import './JogadorDisponivel.css';

export const JogadorDisponivel = ({ atleta, type }) => {
    const { attributes, listeners, setNodeRef, transform } = useDraggable({
        id: atleta.id,
        data: {
            atleta: atleta,
            type: type,
        },
    });

    const style = {
        transform: transform ? `translate3d(${transform.x}px, ${transform.y}px, 0)` : undefined,
    };

    return (
        <div
            ref={setNodeRef}
            style={style}
            {...listeners}
            {...attributes}
            className="jogador-disponivel-card"
        >
            <div className="jogador-avatar">
                {atleta.nome.match(/\b(\w)/g).join('').substring(0, 2)}
            </div>
            <div className="jogador-info">
                <span className="jogador-nome-lista">{atleta.nome}</span>
                <span className="jogador-posicao">{atleta.posicao}</span>
            </div>
            <span className="jogador-numero-lista">#{atleta.numero}</span>
        </div>
    );
};