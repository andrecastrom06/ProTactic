import React from 'react';
import { useDraggable } from '@dnd-kit/core';
import { CSS } from '@dnd-kit/utilities';
import './JogadorNoCampo.css';

export const JogadorNoCampo = ({ atleta }) => {
    const { attributes, listeners, setNodeRef, transform } = useDraggable({
        id: atleta.id,
        data: {
            atleta: atleta,
            type: 'NO_CAMPO',
        },
    });

    // 3. Define a posição do jogador
    const style = {
        // --- MUDANÇA AQUI ---
        // Agora usamos a posição em % que está salva no estado
        top: `${atleta.position.y}%`,
        left: `${atleta.position.x}%`,
        
        // 'transform' é usado pelo dnd-kit para o feedback visual de arrastar
        transform: CSS.Translate.toString(transform),
    };

    return (
        <div
            ref={setNodeRef}
            style={style}
            {...listeners}
            {...attributes}
            className="jogador-no-campo"
        >
            <span className="jogador-numero">{atleta.numero}</span>
            <span className="jogador-nome-campo">{atleta.nome.split(' ')[0]}</span>
        </div>
    );
};