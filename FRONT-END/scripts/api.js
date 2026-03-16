const API_BASE_URL = (function() {
    if (window.location.hostname !== 'localhost' && window.location.hostname !== '127.0.0.1') {
        const serverIp = window.location.hostname;
        return `http://${serverIp}:8080/api`; 
    }
    return 'http://localhost:8080/api';
})();

console.log('🌐 API Base URL:', API_BASE_URL);

function getToken() {
    return localStorage.getItem('token');
}

function getAuthHeaders() {
    const token = getToken();
    return {
        'Content-Type': 'application/json',
        'Authorization': token ? `Bearer ${token}` : ''
    };
}

async function apiRequest(endpoint, method = 'GET', body = null) {
    const url = `${API_BASE_URL}${endpoint}`;
    
    const options = {
        method,
        headers: getAuthHeaders()
    };
    
    if (body) {
        options.body = JSON.stringify(body);
    }
    
    try {
        const response = await fetch(url, options);
        
        if (response.status === 401) {
            localStorage.removeItem('token');
            window.location.href = 'login.html';
            return null;
        }
        
        if (response.status === 204) {
            return null;
        }
        
        const data = await response.json();
        
        if (!response.ok) {
            throw {
                status: response.status,
                message: data.message || 'Erro na requisição',
                errorCode: data.errorCode
            };
        }
        
        return data;
        
    } catch (error) {
        console.error('API Error:', error);
        throw error;
    }
}

async function login(cpf, password, rememberMe) {
    const url = `${API_BASE_URL}/auth/login`;
    
    const response = await fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ cpf, password, rememberMe })
    });
    
    const data = await response.json();
    
    if (!response.ok) {
        throw {
            status: response.status,
            message: data.message || 'Erro no login',
            errorCode: data.errorCode
        };
    }
    
    return data;
}

function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    window.location.href = 'login.html';
}