import React, { useState } from 'react';
import { useAuth } from '../../store/AuthContext';
import { useNavigate } from 'react-router-dom'; 
import './LoginPage.css'; // Estilos

export const LoginPage = () => {
    const [login, setLogin] = useState('');
    const [senha, setSenha] = useState('');
    const [erro, setErro] = useState(null);
    const [carregando, setCarregando] = useState(false);

    const auth = useAuth(); // Acede ao nosso AuthContext
    const navigate = useNavigate(); // Acede ao router

    const handleSubmit = async (e) => {
        e.preventDefault();
        setErro(null);
        setCarregando(true);

        try {
            // 1. Chama a função de login do nosso Context
            await auth.login(login, senha);
            
            // 2. Se funcionar, redireciona para o Dashboard
            navigate('/dashboard'); 
            
        } catch (error) {
            // 3. Se falhar, mostra o erro
            setErro(error.message);
        } finally {
            setCarregando(false);
        }
    };

    return (
        <div className="login-container">
            <div className="login-box">
                <h1>ProTactic</h1>
                <p>Gestão Integrada</p>
                
                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label>Login</label>
                        <input 
                            type="text" 
                            value={login}
                            onChange={(e) => setLogin(e.target.value)}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label>Senha</label>
                        <input 
                            type="password" 
                            value={senha}
                            onChange={(e) => setSenha(e.target.value)}
                            required
                        />
                    </div>
                    
                    {erro && <p className="error-message">{erro}</p>}

                    <button type="submit" className="login-button" disabled={carregando}>
                        {carregando ? "A entrar..." : "Entrar"}
                    </button>
                </form>
            </div>
        </div>
    );
};