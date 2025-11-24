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

    const style = {
        top: `${atleta.position.y}%`,
        left: `${atleta.position.x}%`,
        
        transform: transform 
            ? `${CSS.Translate.toString(transform)} translate(-50%, -50%)` 
            : `translate(-50%, -50%)`,
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