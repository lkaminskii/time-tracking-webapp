// Variáveis globais
let currentUser = null;
let currentPage = 0;
let isLoadingMore = false;
let hasMorePages = true;
let currentSearchDate = '';
let companyLocation = null;
let companyPerimeter = 20;

document.addEventListener('DOMContentLoaded', async function () {

    const token = localStorage.getItem('token');
    if (!token) {
        window.location.href = 'login.html';
        return;
    }

    loadUserData();

    initLocationConfig();

    await loadRecords(true);

    if (currentUser && currentUser.role === 'ADMIN') {
        loadAdminSection();
    }

    document.getElementById('loading').style.display = 'none';
    document.getElementById('mainContent').style.display = 'block';

    setupInfiniteScroll();
});

function formatDate(dateStr) {
    if (!dateStr) return '';
    if (dateStr.includes('/')) return dateStr;

    const [year, month, day] = dateStr.split('-');
    if (!year || !month || !day) return dateStr;
    return `${day}/${month}/${year}`;
}

function translateDayOfWeek(day) {
    const days = {
        'MONDAY': 'Segunda-feira',
        'TUESDAY': 'Terça-feira',
        'WEDNESDAY': 'Quarta-feira',
        'THURSDAY': 'Quinta-feira',
        'FRIDAY': 'Sexta-feira',
        'SATURDAY': 'Sábado',
        'SUNDAY': 'Domingo'
    };
    return days[day] || day || 'Não informado';
}

function appendRecords(records) {
    console.log('📝 Adicionando registros à tabela:', records);
    const tbody = document.getElementById('recordsBody');
    if (!tbody) {
        console.error('❌ Elemento recordsBody não encontrado!');
        return;
    }

    records.forEach(record => {
        const row = document.createElement('tr');

        const formattedDate = record.recordDate ? formatDate(record.recordDate) : 'N/A';
        const formattedTime = record.recordTime || 'N/A';
        const formattedDay = record.dayOfWeek ? translateDayOfWeek(record.dayOfWeek) : 'N/A';
        const employeeName = record.employeeName || 'N/A';
        const companyName = record.companyName || 'N/A';

        row.innerHTML = `
            <td>${formattedDate}</td>
            <td>${formattedTime}</td>
            <td>${formattedDay}</td>
            <td>${employeeName}</td>
            <td>${companyName}</td>
        `;
        tbody.appendChild(row);
    });

    console.log(`✅ ${records.length} registros adicionados à tabela`);
}

function showNoRecords() {
    const tbody = document.getElementById('recordsBody');
    if (!tbody) return;

    tbody.innerHTML = `
        <tr>
            <td colspan="5" style="text-align: center; padding: 2rem; color: #6b7280;">
                Nenhum registro encontrado
            </td>
        </tr>
    `;
}

function setupInfiniteScroll() {
    console.log('📜 Configurando scroll infinito');
    window.addEventListener('scroll', () => {
        const scrollPosition = window.innerHeight + window.scrollY;
        const threshold = document.documentElement.scrollHeight - 1000;

        if (scrollPosition > threshold && !isLoadingMore && hasMorePages) {
            console.log('📜 Scroll detectado, carregando mais...');
            loadRecords();
        }
    });
}

async function searchRecords() {
    const date = document.getElementById('searchDate').value.trim();
    console.log('🔍 Buscando registros por data:', date);

    if (!date) {
        currentSearchDate = '';
        await loadRecords(true);
        return;
    }

    const dateRegex = /^(\d{2})\/(\d{2})\/(\d{4})$/;
    if (!dateRegex.test(date)) {
        alert('Data deve estar no formato dd/mm/aaaa');
        return;
    }

    currentSearchDate = date;
    await loadRecords(true);
}

function loadUserData() {
    const userStr = localStorage.getItem('user');
    if (userStr) {
        currentUser = JSON.parse(userStr);
        document.getElementById('userName').textContent = currentUser.name;
        document.getElementById('userRole').textContent =
            currentUser.role === 'ADMIN' ? 'Administrador' : 'Funcionário';
    }
}

function showClockInModal() {
    document.getElementById('recordModal').style.display = 'flex';
}

function closeModal() {
    console.log('🔒 Fechando modal');
    const modal = document.getElementById('recordModal');
    if (modal) {
        modal.style.display = 'none';
    }
}

function logout() {
    console.log('🚪 Fazendo logout');
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    window.location.href = 'login.html';
}

async function registerTimeRecord() {
    try {
        console.log('📤 Iniciando registro de ponto...');

        const btn = document.querySelector('.btn-clock-in');
        if (btn) {
            btn.textContent = 'Registrando...';
            btn.disabled = true;
        }

        const data = await apiRequest('/employee/time-records', 'POST');

        console.log('📥 Dados recebidos da API:', data);

        if (!data) {
            throw new Error('Resposta vazia da API');
        }

        console.log('Preenchendo modal...');

        const employeeNameEl = document.getElementById('modalEmployeeName');
        const employeePisEl = document.getElementById('modalEmployeePis');
        const companyNameEl = document.getElementById('modalCompanyName');
        const companyCnpjEl = document.getElementById('modalCompanyCnpj');
        const dateEl = document.getElementById('modalDate');
        const timeEl = document.getElementById('modalTime');
        const dayEl = document.getElementById('modalDayOfWeek');

        if (!employeeNameEl || !employeePisEl || !companyNameEl || !companyCnpjEl || !dateEl || !timeEl || !dayEl) {
            console.error('❌ Elementos do modal não encontrados!');
            console.log('Elementos:', { employeeNameEl, employeePisEl, companyNameEl, companyCnpjEl, dateEl, timeEl, dayEl });
        }

        if (employeeNameEl) employeeNameEl.textContent = data.employeeName || 'Não informado';
        if (employeePisEl) employeePisEl.textContent = data.employeePis || 'Não informado';
        if (companyNameEl) companyNameEl.textContent = data.companyName || 'Não informado';
        if (companyCnpjEl) companyCnpjEl.textContent = data.companyCnpj || 'Não informado';

        if (dateEl) {
            dateEl.textContent = data.recordDate ? formatDate(data.recordDate) : 'Não informado';
        }

        if (timeEl) {
            timeEl.textContent = data.recordTime || 'Não informado';
        }

        if (dayEl) {
            dayEl.textContent = data.dayOfWeek ? translateDayOfWeek(data.dayOfWeek) : 'Não informado';
        }

        console.log('✅ Modal preenchido com sucesso');

        const modal = document.getElementById('recordModal');
        if (modal) {
            modal.style.display = 'flex';
            console.log('✅ Modal exibido');
        } else {
            console.error('❌ Modal não encontrado!');
        }

        await loadRecords(true);

        console.log('✅ Ponto registrado com sucesso!');

    } catch (error) {
        console.error('❌ Erro ao registrar ponto:', error);
        alert('Erro ao registrar ponto: ' + (error.message || 'Erro desconhecido'));
    } finally {
        const btn = document.querySelector('.btn-clock-in');
        if (btn) {
            btn.textContent = 'Registrar\nPonto';
            btn.disabled = false;
        }
    }
}

async function loadRecords(reset = false) {
    console.log('📥 Carregando registros... Reset:', reset);

    if (reset) {
        currentPage = 0;
        hasMorePages = true;
        const tbody = document.getElementById('recordsBody');
        if (tbody) tbody.innerHTML = '';
    }

    if (!hasMorePages || isLoadingMore) return;

    isLoadingMore = true;

    if (currentPage > 0) {
        const loadingMore = document.getElementById('loadingMore');
        if (loadingMore) loadingMore.classList.remove('hidden');
    }

    try {
        let endpoint;
        if (currentUser && currentUser.role === 'ADMIN') {
            endpoint = `/admin/time-records/all?page=${currentPage}&size=10`;
        } else {
            endpoint = `/employee/time-records?page=${currentPage}&size=10`;
        }

        if (currentSearchDate) {
            if (currentUser && currentUser.role === 'ADMIN') {
                endpoint = `/admin/time-records/search?date=${currentSearchDate}&page=${currentPage}&size=10`;
            } else {
                endpoint = `/employee/time-records/search?date=${currentSearchDate}&page=${currentPage}&size=10`;
            }
        }

        console.log('🔍 Endpoint:', endpoint);

        const data = await apiRequest(endpoint);
        console.log('📊 Dados recebidos:', data);

        if (data && data.content && data.content.length > 0) {
            appendRecords(data.content); 
            hasMorePages = !data.last;
            currentPage++;
            console.log('✅ Registros carregados. Próxima página:', currentPage);
        } else {
            if (currentPage === 0) {
                showNoRecords(); 
            }
            hasMorePages = false;
            console.log('📭 Nenhum registro encontrado');
        }

    } catch (error) {
        console.error('❌ Erro ao carregar registros:', error);
    } finally {
        isLoadingMore = false;
        const loadingMore = document.getElementById('loadingMore');
        if (loadingMore) loadingMore.classList.add('hidden');
    }
}

function showClockInModal() {
    console.log('📱 Modal aberto - iniciando registro...');

    document.getElementById('modalEmployeeName').textContent = 'Processando...';
    document.getElementById('modalEmployeePis').textContent = 'Processando...';
    document.getElementById('modalCompanyName').textContent = 'Processando...';
    document.getElementById('modalCompanyCnpj').textContent = 'Processando...';
    document.getElementById('modalDate').textContent = 'Processando...';
    document.getElementById('modalTime').textContent = 'Processando...';
    document.getElementById('modalDayOfWeek').textContent = 'Processando...';

    document.getElementById('recordModal').style.display = 'flex';

    registerTimeRecord();
}

async function loadAdminSection() {
    const adminSection = document.getElementById('adminSection');
    adminSection.classList.remove('hidden');

    try {
        const data = await apiRequest('/admin/employees?page=0&size=10');

        if (data.content && data.content.length > 0) {
            displayEmployees(data.content);
        }

    } catch (error) {
        console.error('Erro ao carregar funcionários:', error);
    }
}

function displayEmployees(employees) {
    console.log('📋 Exibindo funcionários:', employees);
    const container = document.getElementById('employeesList');

    if (!container) {
        console.error('❌ Container employeesList não encontrado');
        return;
    }

    if (!employees || employees.length === 0) {
        container.innerHTML = '<p style="text-align: center; color: var(--text-tertiary); padding: 2rem;">Nenhum funcionário cadastrado</p>';
        return;
    }

    let html = '<div class="employees-grid">';

    employees.forEach(emp => {
        const cpfFormatado = emp.cpf ? emp.cpf.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4') : 'N/A';
        const pisFormatado = emp.pis ? emp.pis.replace(/(\d{3})(\d{5})(\d{2})(\d{1})/, '$1.$2.$3-$4') : 'N/A';
        const cnpjFormatado = emp.companyCnpj ? emp.companyCnpj.replace(/(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})/, '$1.$2.$3/$4-$5') : 'N/A';

        html += `
            <div class="employee-card" data-cpf="${emp.cpf}">
                <div class="employee-header">
                    <span class="employee-name" title="${emp.name || 'N/A'}">${emp.name || 'N/A'}</span>
                    <span class="employee-badge">${emp.role === 'ADMIN' ? 'Admin' : 'Funcionário'}</span>
                </div>
                
                <div class="employee-details">
                    <div>
                        <i class="fas fa-id-card"></i>
                        <strong>CPF:</strong> <span>${cpfFormatado}</span>
                    </div>
                    <div>
                        <i class="fas fa-barcode"></i>
                        <strong>PIS:</strong> <span>${pisFormatado}</span>
                    </div>
                    <div>
                        <i class="fas fa-building"></i>
                        <strong>Empresa:</strong> <span>${emp.companyName || 'N/A'}</span>
                    </div>
                    <div>
                        <i class="fas fa-file-invoice"></i>
                        <strong>CNPJ:</strong> <span>${cnpjFormatado}</span>
                    </div>
                </div>
                
                <div class="employee-stats">
                    <div class="stat">
                        <div class="stat-value">${emp.totalTimeRecords || 0}</div>
                        <div class="stat-label">Registros</div>
                    </div>
                    <div class="stat">
                        <div class="stat-value" style="color: var(--secondary);">●</div>
                        <div class="stat-label">Ativo</div>
                    </div>
                    <div class="stat">
                        <div class="stat-value">${emp.createdAt ? new Date(emp.createdAt).toLocaleDateString() : '-'}</div>
                        <div class="stat-label">Desde</div>
                    </div>
                </div>
                
                <div class="employee-actions">
                    <button class="btn-icon edit" onclick="openEditModal('${emp.cpf}')" title="Editar funcionário">
                        <i class="fas fa-edit"></i>
                    </button>
                    ${emp.role !== 'ADMIN' ? `
                    <button class="btn-icon delete" onclick="confirmDeleteEmployee('${emp.cpf}', '${emp.name}')" title="Excluir funcionário">
                        <i class="fas fa-trash-alt"></i>
                    </button>
                    ` : ''}
                </div>
            </div>
        `;
    });

    html += '</div>';
    container.innerHTML = html;
    console.log('✅ Lista de funcionários atualizada');
}

async function openEditModal(cpf) {
    console.log('✏️ Abrindo edição para funcionário CPF:', cpf);

    try {
        const employee = await apiRequest(`/admin/employees/${cpf}`);
        console.log('📦 Dados do funcionário:', employee);

        document.getElementById('editEmployeeId').value = employee.cpf || '';
        document.getElementById('editCpf').value = employee.cpf || '';
        document.getElementById('editName').value = employee.name || '';
        document.getElementById('editPis').value = employee.pis || '';
        document.getElementById('editPassword').value = ''; // Sempre vazio por segurança
        document.getElementById('editCompanyName').value = employee.companyName || '';
        document.getElementById('editCompanyCnpj').value = employee.companyCnpj || '';

        document.getElementById('editEmployeeModal').style.display = 'flex';

    } catch (error) {
        console.error('❌ Erro ao carregar dados do funcionário:', error);
        alert('Erro ao carregar dados do funcionário');
    }
}

function closeEditModal() {
    console.log('🔒 Fechando modal de edição');
    document.getElementById('editEmployeeModal').style.display = 'none';
}

async function updateEmployee() {
    console.log('📤 Atualizando funcionário...');

    const originalCpf = document.getElementById('editEmployeeId').value;
    const employeeData = {
        cpf: document.getElementById('editCpf').value.replace(/\D/g, ''),
        name: document.getElementById('editName').value.trim(),
        pis: document.getElementById('editPis').value.replace(/\D/g, ''),
        password: document.getElementById('editPassword').value,
        companyName: document.getElementById('editCompanyName').value.trim(),
        companyCnpj: document.getElementById('editCompanyCnpj').value.replace(/\D/g, '')
    };

    const errors = [];
    if (!employeeData.cpf || employeeData.cpf.length !== 11) errors.push('CPF deve ter 11 dígitos');
    if (!employeeData.name) errors.push('Nome é obrigatório');
    if (!employeeData.pis || employeeData.pis.length !== 11) errors.push('PIS deve ter 11 dígitos');
    if (!employeeData.companyName) errors.push('Nome da empresa é obrigatório');
    if (!employeeData.companyCnpj || employeeData.companyCnpj.length !== 14) errors.push('CNPJ deve ter 14 dígitos');

    if (errors.length > 0) {
        alert('Erros no formulário:\n- ' + errors.join('\n- '));
        return;
    }

    if (!employeeData.password) {
        delete employeeData.password;
    }

    console.log('📦 Dados a serem enviados:', employeeData);

    try {
        await apiRequest(`/admin/employees/${originalCpf}`, 'PUT', employeeData);

        alert('Funcionário atualizado com sucesso!');
        closeEditModal();

        await loadAdminSection();

    } catch (error) {
        console.error('❌ Erro ao atualizar funcionário:', error);
        alert('Erro ao atualizar: ' + (error.message || 'Erro desconhecido'));
    }
}


function confirmDeleteEmployee(cpf, name) {
    console.log('🗑️ Confirmar exclusão do funcionário:', cpf, name);

    document.getElementById('confirmMessage').textContent = `Excluir funcionário?`;
    document.getElementById('confirmSubmessage').textContent = `Tem certeza que deseja excluir os dados de ${name}? Esta ação não pode ser desfeita.`;

    const confirmBtn = document.getElementById('confirmDeleteBtn');
    confirmBtn.onclick = () => deleteEmployee(cpf);

    document.getElementById('confirmDeleteModal').style.display = 'flex';
}

function closeConfirmModal() {
    console.log('🔒 Fechando modal de confirmação');
    document.getElementById('confirmDeleteModal').style.display = 'none';
}

async function deleteEmployee(cpf) {
    console.log('🗑️ Excluindo funcionário CPF:', cpf);

    try {
        closeConfirmModal();

        document.getElementById('loading').style.display = 'block';

        await apiRequest(`/admin/employees/${cpf}`, 'DELETE');

        alert('Funcionário excluído com sucesso!');

        await loadAdminSection();

    } catch (error) {
        console.error('❌ Erro ao excluir funcionário:', error);
        alert('Erro ao excluir: ' + (error.message || 'Erro desconhecido'));
    } finally {
        document.getElementById('loading').style.display = 'none';
    }
}

function initLocationConfig() {
    console.log('📍 Inicializando configuração de localização');

    loadSavedLocation();

    if (currentUser && currentUser.role === 'ADMIN') {
        document.getElementById('locationConfig').style.display = 'flex';
    }

    getCurrentDeviceLocation();
}

function loadSavedLocation() {
    const savedLat = localStorage.getItem('companyLatitude');
    const savedLng = localStorage.getItem('companyLongitude');
    const savedPerimeter = localStorage.getItem('companyPerimeter');

    if (savedLat && savedLng) {
        companyLocation = {
            latitude: parseFloat(savedLat),
            longitude: parseFloat(savedLng)
        };
        companyPerimeter = savedPerimeter ? parseFloat(savedPerimeter) : 20;

        updateLocationDisplay();
        console.log('📍 Localização carregada:', companyLocation, 'Perímetro:', companyPerimeter);
    }
}

function updateLocationDisplay() {
    const coordsElement = document.getElementById('currentCoords');
    if (coordsElement) {
        if (companyLocation) {
            coordsElement.innerHTML = `
                <i class="fas fa-map-pin"></i> 
                ${companyLocation.latitude.toFixed(6)}, ${companyLocation.longitude.toFixed(6)} 
                | ${companyPerimeter}m
            `;
        } else {
            coordsElement.innerHTML = '<i class="fas fa-map-marker-alt"></i> Não configurado';
        }
    }
}

function openLocationModal() {
    console.log('📍 Abrindo modal de configuração');

    if (companyLocation) {
        document.getElementById('latitude').value = companyLocation.latitude;
        document.getElementById('longitude').value = companyLocation.longitude;
    } else {
        document.getElementById('latitude').value = '';
        document.getElementById('longitude').value = '';
    }
    document.getElementById('perimeter').value = companyPerimeter;

    document.getElementById('locationModal').style.display = 'flex';
}

function closeLocationModal() {
    document.getElementById('locationModal').style.display = 'none';
}

function getCurrentDeviceLocation() {
    console.log('📍 Obtendo localização do dispositivo...');

    if (!navigator.geolocation) {
        console.error('❌ Geolocalização não suportada');
        alert('Seu navegador não suporta geolocalização');
        return;
    }

    navigator.geolocation.getCurrentPosition(
        (position) => {
            console.log('📍 Localização obtida:', position.coords);

            document.getElementById('currentDeviceLat').textContent = position.coords.latitude.toFixed(6);
            document.getElementById('currentDeviceLng').textContent = position.coords.longitude.toFixed(6);

            if (confirm('Usar esta localização como ponto da empresa?')) {
                document.getElementById('latitude').value = position.coords.latitude;
                document.getElementById('longitude').value = position.coords.longitude;
            }
        },
        (error) => {
            console.error('❌ Erro ao obter localização:', error);
            let errorMessage = 'Erro ao obter localização: ';
            switch (error.code) {
                case error.PERMISSION_DENIED:
                    errorMessage += 'Permissão negada';
                    break;
                case error.POSITION_UNAVAILABLE:
                    errorMessage += 'Posição indisponível';
                    break;
                case error.TIMEOUT:
                    errorMessage += 'Tempo esgotado';
                    break;
                default:
                    errorMessage += 'Erro desconhecido';
            }
            alert(errorMessage);
        },
        {
            enableHighAccuracy: true,
            timeout: 10000,
            maximumAge: 0
        }
    );
}

function saveLocationConfig() {
    console.log('📍 Salvando configuração de localização');

    const latitude = parseFloat(document.getElementById('latitude').value);
    const longitude = parseFloat(document.getElementById('longitude').value);
    const perimeter = parseInt(document.getElementById('perimeter').value);

    // Validações
    if (isNaN(latitude) || isNaN(longitude)) {
        alert('Preencha latitude e longitude válidas');
        return;
    }

    if (latitude < -90 || latitude > 90) {
        alert('Latitude deve estar entre -90 e 90');
        return;
    }

    if (longitude < -180 || longitude > 180) {
        alert('Longitude deve estar entre -180 e 180');
        return;
    }

    if (isNaN(perimeter) || perimeter < 1) {
        alert('Perímetro deve ser maior que 0');
        return;
    }

    localStorage.setItem('companyLatitude', latitude.toString());
    localStorage.setItem('companyLongitude', longitude.toString());
    localStorage.setItem('companyPerimeter', perimeter.toString());

    companyLocation = { latitude, longitude };
    companyPerimeter = perimeter;

    updateLocationDisplay();

    closeLocationModal();

    alert('✅ Localização configurada com sucesso!');
}

function calculateDistance(lat1, lon1, lat2, lon2) {
    const R = 6371e3;
    const φ1 = lat1 * Math.PI / 180;
    const φ2 = lat2 * Math.PI / 180;
    const Δφ = (lat2 - lat1) * Math.PI / 180;
    const Δλ = (lon2 - lon1) * Math.PI / 180;

    const a = Math.sin(Δφ / 2) * Math.sin(Δφ / 2) +
        Math.cos(φ1) * Math.cos(φ2) *
        Math.sin(Δλ / 2) * Math.sin(Δλ / 2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return R * c; 
}

function checkIfWithinPerimeter(userLat, userLon) {
    if (!companyLocation) {
        console.log('📍 Localização da empresa não configurada');
        return true; 
    }

    const distance = calculateDistance(
        userLat, userLon,
        companyLocation.latitude, companyLocation.longitude
    );

    console.log(`📍 Distância da empresa: ${distance.toFixed(2)}m (perímetro: ${companyPerimeter}m)`);

    return distance <= companyPerimeter;
}

async function registerTimeRecordWithLocation() {
    try {
        console.log('📍 Verificando geolocalização antes do registro...');

        if (!navigator.geolocation) {
            alert('Seu navegador não suporta geolocalização');
            return;
        }

        const btn = document.querySelector('.btn-clock-in');
        if (btn) {
            btn.textContent = 'Verificando local...';
            btn.disabled = true;
        }

        navigator.geolocation.getCurrentPosition(
            async (position) => {
                try {
                    const userLat = position.coords.latitude;
                    const userLon = position.coords.longitude;

                    console.log(`📍 Sua posição: ${userLat}, ${userLon}`);

                    if (!checkIfWithinPerimeter(userLat, userLon)) {
                        const distance = calculateDistance(
                            userLat, userLon,
                            companyLocation.latitude, companyLocation.longitude
                        );
                        alert(`❌ Você está a ${Math.round(distance)}m da empresa.\n` +
                            `Permitido apenas dentro de ${companyPerimeter}m.`);
                        return;
                    }

                    console.log('✅ Dentro do perímetro! Registrando ponto...');

                    await registerTimeRecord();

                } catch (error) {
                    console.error('❌ Erro ao registrar ponto:', error);
                    alert('Erro ao registrar ponto: ' + (error.message || 'Erro desconhecido'));
                } finally {
                    if (btn) {
                        btn.innerHTML = '<i class="fas fa-clock"></i><span>Registrar<br>Ponto</span>';
                        btn.disabled = false;
                    }
                }
            },
            (error) => {
                console.error('❌ Erro de geolocalização:', error);

                if (btn) {
                    btn.innerHTML = '<i class="fas fa-clock"></i><span>Registrar<br>Ponto</span>';
                    btn.disabled = false;
                }

                let errorMessage = 'Erro ao obter localização: ';
                switch (error.code) {
                    case error.PERMISSION_DENIED:
                        errorMessage += 'Permissão negada. Permita o acesso à localização.';
                        break;
                    case error.POSITION_UNAVAILABLE:
                        errorMessage += 'Localização indisponível.';
                        break;
                    case error.TIMEOUT:
                        errorMessage += 'Tempo esgotado. Tente novamente.';
                        break;
                    default:
                        errorMessage += 'Erro desconhecido.';
                }
                alert(errorMessage);
            },
            {
                enableHighAccuracy: true,
                timeout: 10000,
                maximumAge: 0
            }
        );

    } catch (error) {
        console.error('❌ Erro:', error);
    }
}