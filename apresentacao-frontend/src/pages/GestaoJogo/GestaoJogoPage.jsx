import React, { useState, useRef } from 'react';
import { DndContext, closestCenter } from '@dnd-kit/core';

import { CampoTatico } from './components/CampoTatico';
import { ListaAtletas } from './components/ListaAtletas';
import { AbaAtribuirNotas } from './components/AbaAtribuirNotas'; // 1. Importe o novo componente
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
    const [abaAtiva, setAbaAtiva] = useState('ESCALACAO');

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

        const campoRect = campoTaticoRef.current?.getBoundingClientRect();
        if (!campoRect) return;

        if (tipoOrigem === 'NO_CAMPO' && idDestino === 'CAMPO_TATICO') {
            const percentDeltaX = (delta.x / campoRect.width) * 100;
            const percentDeltaY = (delta.y / campoRect.height) * 100;
            setAtletasNoCampo((items) =>
                items.map((item) =>
                    item.id === active.id
                        ? { ...item, position: { 
                              x: item.position.x + percentDeltaX,
                              y: item.position.y + percentDeltaY 
                          } }
                        : item
                )
            );
            return;
        }
        if ( (tipoOrigem === 'DISPONIVEL' && idDestino === 'DISPONIVEIS_LIST') ||
             (tipoOrigem === 'RESERVA' && idDestino === 'RESERVAS_LIST') ) {
            return;
        }
        if (tipoOrigem === 'DISPONIVEL') {
            setAtletasDisponiveis((prev) => prev.filter((a) => a.id !== atletaArrastado.id));
        } else if (tipoOrigem === 'RESERVA') {
            setReservas((prev) => prev.filter((a) => a.id !== atletaArrastado.id));
        } else if (tipoOrigem === 'NO_CAMPO') {
            setAtletasNoCampo((prev) => prev.filter((a) => a.id !== atletaArrastado.id));
        }
        const { position, ...atletaLimpo } = atletaArrastado;
        if (idDestino === 'CAMPO_TATICO') {
            let newPosition = { x: 50, y: 50 }; 
            const itemRect = event.active.rect.current.translated;
            if (itemRect) {
                let relativeX = itemRect.left - campoRect.left;
                let relativeY = itemRect.top - campoRect.top;
                relativeX -= (itemRect.width / 2);
                relativeY -= (itemRect.height / 2);
                const percentX = (relativeX / campoRect.width) * 100;
                const percentY = (relativeY / campoRect.height) * 100;
                newPosition = { x: percentX, y: percentY };
            }
            setAtletasNoCampo((prev) => [
                ...prev,
                { ...atletaLimpo, position: newPosition }
            ]);
        } 
        else if (idDestino === 'DISPONIVEIS_LIST') {
            setAtletasDisponiveis((prev) => [...prev, atletaLimpo]);
        } else if (idDestino === 'RESERVAS_LIST') {
            setReservas((prev) => [...prev, atletaLimpo]);
        }
    };

    const todosAtletas = [...atletasNoCampo, ...reservas];

    return (
        <DndContext collisionDetection={closestCenter} onDragEnd={handleDragEnd}>
            <div className="gestao-jogo-page">
                <header className="gestao-jogo-header">
                    <h2>Gestão de Jogo</h2>
                    <nav className="gestao-jogo-tabs">
                        <span 
                            className={`tab-item ${abaAtiva === 'ESCALACAO' ? 'active' : ''}`}
                            onClick={() => setAbaAtiva('ESCALACAO')}
                        >
                            Escalação Tática
                        </span>
                        <span 
                            className={`tab-item ${abaAtiva === 'NOTAS' ? 'active' : ''}`}
                            onClick={() => setAbaAtiva('NOTAS')}
                        >
                            Atribuir Notas
                        </span>
                        <span className="tab-item">Relatório de Desempenho</span>
                    </nav>
                </header>
                
                <div className="gestao-jogo-content">
                    {abaAtiva === 'ESCALACAO' && (
                        <>
                            <div className="campo-column">
                                <CampoTatico 
                                    atletas={atletasNoCampo} 
                                    fieldRef={campoTaticoRef} 
                                />
                            </div>
                            <div className="lista-column">
                                <ListaAtletas
                                    disponiveis={atletasDisponiveis}
                                    reservas={reservas}
                                />
                            </div>
                        </>
                    )}

                    {abaAtiva === 'NOTAS' && (
                        <AbaAtribuirNotas atletas={atletasDisponiveis} />
                    )}
                </div>
            </div>
        </DndContext>
    );
};