import React from 'react';
import { useDroppable } from '@dnd-kit/core';
import { JogadorNoCampo } from './JogadorNoCampo';
import './CampoTatico.css';

// 1. Receba o 'fieldRef' como prop
export const CampoTatico = ({ atletas, fieldRef }) => {
    
    // 2. Pegue o 'setNodeRef' do dnd-kit
    const { setNodeRef } = useDroppable({
        id: 'CAMPO_TATICO',
    });

    // 3. Crie uma função que combina os dois refs
    const combinedRef = (node) => {
        fieldRef.current = node; // Para o nosso cálculo de posição
        setNodeRef(node); // Para o dnd-kit saber que é um 'droppable'
    };

    return (
        <div className="campo-container">
            <h3>Campo Tático</h3>
            {/* 4. Use o 'combinedRef' aqui */}
            <div ref={combinedRef} className="campo-tatico-bg">
                
                {/* A tag <img> foi removida.
                  O CSS está cuidando da imagem de fundo.
                */}
                
                {/* 5. Mapeia e renderiza os jogadores */}
                {atletas.map((atleta) => (
                    <JogadorNoCampo key={atleta.id} atleta={atleta} />
                ))}
            </div>
        </div>
    );
};