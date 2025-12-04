import React, { useState } from 'react';
import Swal from 'sweetalert2'; // Importação do SweetAlert2
import { useAuth } from '../../store/AuthContext';
import { useNavigate } from 'react-router-dom'; 
import './LoginPage.css'; // Estilos

export const LoginPage = () => {
    const [login, setLogin] = useState('');
    const [senha, setSenha] = useState('');
    const [carregando, setCarregando] = useState(false);

    const auth = useAuth();
    const navigate = useNavigate(); 

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        if (!login || !senha) {
            Swal.fire({
                title: 'Atenção',
                text: 'Por favor, preencha todos os campos.',
                icon: 'warning',
                confirmButtonText: 'OK'
            });
            return;
        }

        setCarregando(true);

        try {
            await auth.login(login, senha);
            
            await Swal.fire({
                title: 'Bem-vindo!',
                text: 'Login realizado com sucesso.',
                icon: 'success',
                timer: 1500,
                showConfirmButton: false
            });
            
            navigate('/dashboard'); 
            
        } catch (error) {
            console.error(error);
            Swal.fire({
                title: 'Falha no Login',
                text: error.message || 'Usuário ou senha incorretos.',
                icon: 'error',
                confirmButtonText: 'Tentar Novamente'
            });
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
                            disabled={carregando}
                        />
                    </div>
                    <div className="form-group">
                        <label>Senha</label>
                        <input 
                            type="password" 
                            value={senha}
                            onChange={(e) => setSenha(e.target.value)}
                            required
                            disabled={carregando}
                        />
                    </div>
                    
                    <button type="submit" className="login-button" disabled={carregando}>
                        {carregando ? "Entrando..." : "Entrar"}
                    </button>
                </form>
            </div>
        </div>
    );
};