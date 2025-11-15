import React, { useState, useRef } from 'react';
import { DndContext, closestCenter } from '@dnd-kit/core';

import { CampoTatico } from './components/CampoTatico';
import { ListaAtletas } from './components/ListaAtletas';

import './GestaoJogoPage.css';

// --- Dados de Exemplo (MOCK) ---
const MOCK_DISPONIVEIS = [
    { id: 'a1', nome: 'Rokenedy', posicao: 'Goleiro', numero: 1 },
    { id: 'a2', nome: 'PH', posicao: 'Lateral', numero: 2 },
    { id: 'a3', nome: 'Leo Pele', posicao: 'Zagueiro', numero: 3 },
    { id: 'a4', nome: 'Maicon', posicao: 'Zagueiro', numero: 4 },
    { id: 'a5', nome: 'Carius', posicao: 'Lateral', numero: 5 },
    { id: 'a6', nome: 'Hugo Moura', posicao: 'Meio-campo', numero: 6 },
    { id: 'a7', nome: 'Balotelli', posicao: 'Meio-campo', numero: 7 },
    { id: 'a8', nome: 'Carlos Alberto', posicao: 'Atacante', numero: 11 },
    { id: 'a9', nome: 'Ariel Nahuelpan', posicao: 'Atacante', numero: 99 },
    { id: 'a10', nome: 'Lucas Lima', posicao: 'Meio-campo', numero: 10 },
    { id: 'a11', nome: 'Rayan', posicao: 'Atacante', numero: 9 }


];

const MOCK_RESERVAS = [
    { id: 'r1', nome: 'Bruno Lima', posicao: 'Goleiro', numero: 12 },
    { id: 'r2', nome: 'Rodrigo Alves', posicao: 'Zagueiro', numero: 13 },
    { id: 'r3', nome: 'Paulo Henrique', posicao: 'Meio-campo', numero: 14 },
];
// ----------------------------------

export const GestaoJogoPage = () => {
    const [atletasDisponiveis, setAtletasDisponiveis] = useState(MOCK_DISPONIVEIS);
    const [reservas, setReservas] = useState(MOCK_RESERVAS);
    const [atletasNoCampo, setAtletasNoCampo] = useState([]);

    const campoTaticoRef = useRef(null);

    const handleDragEnd = (event) => {
        const { active, over, delta } = event;
        if (!over) return; 

        const atletaArrastado = active.data.current.atleta;
        const tipoOrigem = active.data.current.type; 
        const idDestino = over.id; 

        // Pega as dimensões do campo. É crucial para calcular %
        const campoRect = campoTaticoRef.current?.getBoundingClientRect();
        if (!campoRect) return; // Não faz nada se o campo não for encontrado

        // Caso 1: Mover um jogador DENTRO do campo
        if (tipoOrigem === 'NO_CAMPO' && idDestino === 'CAMPO_TATICO') {
            
            // --- MUDANÇA AQUI ---
            // Converte o 'delta' (movimento em pixels) para porcentagem
            const percentDeltaX = (delta.x / campoRect.width) * 100;
            const percentDeltaY = (delta.y / campoRect.height) * 100;

            setAtletasNoCampo((items) =>
                items.map((item) =>
                    item.id === active.id
                        ? { ...item, position: { 
                              x: item.position.x + percentDeltaX, // Soma % com %
                              y: item.position.y + percentDeltaY  // Soma % com %
                          } }
                        : item
                )
            );
            return;
        }

        // Se a origem e o destino forem a mesma lista, não faz nada
        if ( (tipoOrigem === 'DISPONIVEL' && idDestino === 'DISPONIVEIS_LIST') ||
             (tipoOrigem === 'RESERVA' && idDestino === 'RESERVAS_LIST') ) {
            return;
        }

        // --- Casos de Mover ENTRE listas ---

        // 1. Remove o atleta da lista de Origem
        if (tipoOrigem === 'DISPONIVEL') {
            setAtletasDisponiveis((prev) => prev.filter((a) => a.id !== atletaArrastado.id));
        } else if (tipoOrigem === 'RESERVA') {
            setReservas((prev) => prev.filter((a) => a.id !== atletaArrastado.id));
        } else if (tipoOrigem === 'NO_CAMPO') {
            setAtletasNoCampo((prev) => prev.filter((a) => a.id !== atletaArrastado.id));
        }
        
        const { position, ...atletaLimpo } = atletaArrastado;

        // 2. Adiciona o atleta na lista de Destino
        if (idDestino === 'CAMPO_TATICO') {
            
            // --- MUDANÇA AQUI ---
            // Posição de fallback (em %)
            let newPosition = { x: 50, y: 50 }; 

            const itemRect = event.active.rect.current.translated;

            if (itemRect) {
                // Calcula a posição X e Y *relativa* ao campo (em pixels)
                let relativeX = itemRect.left - campoRect.left;
                let relativeY = itemRect.top - campoRect.top;

                // Centraliza o pino no cursor
                relativeX -= (itemRect.width / 2);
                relativeY -= (itemRect.height / 2);

                // Converte a posição final em pixels para PORCENTAGEM
                const percentX = (relativeX / campoRect.width) * 100;
                const percentY = (relativeY / campoRect.height) * 100;
                
                newPosition = { x: percentX, y: percentY };
            }

            setAtletasNoCampo((prev) => [
                ...prev,
                { ...atletaLimpo, position: newPosition } // Salva a posição em %
            ]);
        } 
        else if (idDestino === 'DISPONIVEIS_LIST') {
            setAtletasDisponiveis((prev) => [...prev, atletaLimpo]);
        } else if (idDestino === 'RESERVAS_LIST') {
            setReservas((prev) => [...prev, atletaLimpo]);
        }
    };

    return (
        <DndContext collisionDetection={closestCenter} onDragEnd={handleDragEnd}>
            <div className="gestao-jogo-page">
                {/* Abas de navegação */}
                <header className="gestao-jogo-header">
                    <h2>Gestão de Jogo</h2>
                    <nav className="gestao-jogo-tabs">
                        <span className="tab-item active">Escalação Tática</span>
                        <span className="tab-item">Atribuir Metas</span>
                        <span className="tab-item">Relatório de Desempenho</span>
                    </nav>
                </header>
                
                <div className="gestao-jogo-content">
                    {/* Coluna da Esquerda (Campo) */}
                    <div className="campo-column">
                        <CampoTatico 
                            atletas={atletasNoCampo} 
                            fieldRef={campoTaticoRef} 
                        />
                    </div>

                    {/* Coluna da Direita (Listas) */}
                    <div className="lista-column">
                        <ListaAtletas
                            disponiveis={atletasDisponiveis}
                            reservas={reservas}
                        />
                    </div>
                </div>
            </div>
        </DndContext>
    );
};