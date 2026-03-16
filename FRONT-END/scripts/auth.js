document.addEventListener('DOMContentLoaded', function() {
    
    const token = localStorage.getItem('token');
    if (token) {
        window.location.href = 'dashboard.html';
        return;
    }
    
    const loginForm = document.getElementById('loginForm');
    const alertDiv = document.getElementById('alert');
    const loadingDiv = document.getElementById('loading');
    const btnLogin = document.getElementById('btnLogin');
    
    function showMessage(message, isError = true) {
        alertDiv.textContent = message;
        alertDiv.className = `alert ${isError ? 'alert-error' : 'alert-success'}`;
        alertDiv.style.display = 'block';
        
        setTimeout(() => {
            alertDiv.style.display = 'none';
        }, 5000);
    }
    
    function setLoading(isLoading) {
        if (isLoading) {
            loadingDiv.style.display = 'block';
            btnLogin.disabled = true;
            btnLogin.style.opacity = '0.7';
        } else {
            loadingDiv.style.display = 'none';
            btnLogin.disabled = false;
            btnLogin.style.opacity = '1';
        }
    }
    
    loginForm.addEventListener('submit', async function(e) {
        e.preventDefault();
        
        const cpf = document.getElementById('cpf').value.trim();
        const password = document.getElementById('password').value;
        const rememberMe = document.getElementById('rememberMe').checked;
        
        if (!cpf || !password) {
            showMessage('Preencha todos os campos');
            return;
        }
        
        if (cpf.length !== 11) {
            showMessage('CPF deve ter 11 dígitos');
            return;
        }
        
        setLoading(true);
        
        try {
            const data = await login(cpf, password, rememberMe);
            
            localStorage.setItem('token', data.token);
            localStorage.setItem('user', JSON.stringify({
                cpf: data.cpf,
                name: data.name,
                role: data.role
            }));
            
            showMessage('Login realizado com sucesso! Redirecionando...', false);
            
            setTimeout(() => {
                window.location.href = 'dashboard.html';
            }, 1500);
            
        } catch (error) {
            console.error('Erro no login:', error);
            showMessage(error.message || 'Erro ao fazer login. Tente novamente.');
        } finally {
            setLoading(false);
        }
    });
    
    const cpfInput = document.getElementById('cpf');
    cpfInput.addEventListener('input', function(e) {
        let value = e.target.value.replace(/\D/g, '');
        if (value.length > 11) value = value.slice(0, 11);
        e.target.value = value;
    });
});