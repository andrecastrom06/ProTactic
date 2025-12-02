import React from 'react';
// 1. Importa o nosso gestor de rotas
import { AppRoutes } from './routes';
// (Podes apagar a importação do './App.css' se quiseres)

function App() {
  return (
    <div className="App">
      <AppRoutes />
    </div>
  );
}

export default App;