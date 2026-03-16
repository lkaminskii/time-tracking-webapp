# ⚡ Quick Start Guide - Time Tracking System

Guia rápido para começar a desenvolver e usar o projeto.

---

## 5 Minutos para o Seu Primeiro Run

### 1️⃣ Pré-requisitos (5 min)

**Instalar:**
- Java 17+ - [Download](https://www.oracle.com/java/technologies/downloads/#java17)
- MySQL Server - [Download](https://dev.mysql.com/downloads/mysql/)
- Git - [Download](https://git-scm.com/)

**Verificar Instalação:**
```bash
java -version
mysql --version
git --version
```

### 2️⃣ Clonar Repositório (2 min)

```bash
git clone https://github.com/seu-usuario/time-tracking.git
cd time-tracking
```

### 3️⃣ Configurar Banco de Dados (3 min)

**Abra MySQL:**
```bash
mysql -u root -p
```

**Execute:**
```sql
CREATE DATABASE time_tracking_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
GRANT ALL PRIVILEGES ON time_tracking_db.* TO 'root'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

### 4️⃣ Executar Back-End (2 min)

```bash
cd BACK-END/time-tracking
./mvnw spring-boot:run
```

**Esperado:**
```
...
2025-01-16 08:30:00 ... Started TimeTrackingApplication in 5.123 seconds
```

### 5️⃣ Executar Front-End (1 min)

**Em outro terminal:**
```bash
cd FRONT-END
python -m http.server 8000
```

Ou use Live Server no VS Code.

### 6️⃣ Acessar Aplicação

Abra no navegador: **http://localhost:8000**

**Login:**
- CPF: `12345678900`
- Senha: `admin123`

---

## 🔧 Comandos Úteis

### Back-End

```bash
# Compilar projeto
./mvnw clean compile

# Executar testes
./mvnw test

# Criar JAR executável
./mvnw clean package

# Executar JAR
java -jar target/time-tracking-0.0.1-SNAPSHOT.jar

# Limpar dependências e rebuild
./mvnw clean install

# Ver dependências
./mvnw dependency:tree
```

### Front-End

```bash
# Inspecionar com Live Server (VS Code)
Ctrl+Shift+P → "Go Live"

# Python server
python -m http.server 3000

# Node.js server
npx http-server -p 3000 -c-1
```

### Banco de Dados

```bash
# Conectar ao MySQL
mysql -u root -p time_tracking_db

# Ver estrutura
SHOW TABLES;

# Ver funcionários
SELECT * FROM employees;

# Ver registros de ponto
SELECT * FROM time_records;

# Limpar banco (CUIDADO!)
DROP DATABASE time_tracking_db;
```

---

## 🐛 Troubleshooting

### Problema: "Connection refused: localhost:3306"

**Causa:** MySQL não está rodando

**Solução:**
```bash
# Windows - Iniciar serviço MySQL
net start MySQL80

# macOS - Iniciar MySQL
brew services start mysql

# Linux - Iniciar MySQL
sudo systemctl start mysql
```

---

### Problema: "Failed to bind to port 8080"

**Causa:** Porta 8080 já está em uso

**Solução:**
```bash
# Windows - Encontrar processo na porta
netstat -ano | findstr :8080

# macOS/Linux - Encontrar processo
lsof -i :8080

# Usar porta diferente
java -jar target/time-tracking-0.0.1-SNAPSHOT.jar --server.port=8081
```

---

### Problema: "ERROR: Access denied for user 'root'@'localhost'"

**Causa:** Senha do MySQL incorreta

**Solução:**
1. Abra `BACK-END/time-tracking/src/main/resources/application.properties`
2. Verifique:
```properties
spring.datasource.username=root      # Seu usuário
spring.datasource.password=root      # Sua senha
```

---

### Problema: Token Expirado no Frontend

**Causa:** JWT token expirou (24 horas)

**Solução:**
```javascript
// No console do navegador
localStorage.clear();

// Faça login novamente
```

---

### Problema: "CORS error" ao acessar API

**Causa:** Frontend e Backend em portas diferentes

**Solução:**
Frontend já está configurado para aceitar CORS. Verifique:
- Backend rodando em: `http://localhost:8080`
- Frontend rodando em: `http://localhost:8000` ou outra porta

Se mudar porta, edite `FRONT-END/scripts/api.js`:
```javascript
const API_BASE_URL = 'http://localhost:8080/api';
```

---

### Problema: "ClassNotFoundException: javax.persistence"

**Causa:** Jakarta EE vs Java EE incompatibility

**Solução:**
Verifique `pom.xml`:
```xml
<version>4.0.3</version>  <!-- Deve ser Spring Boot 4.0+ -->
```

---

### Problema: Dados não aparecem no Dashboard

**Causa:** Funcionário não possui registros

**Solução:**
1. Crie um novo registro clicando "Registrar Ponto"
2. Verifique banco de dados:
```sql
SELECT * FROM time_records;
```

---

## 📝 Estrutura de Pastas Explicada

```
time-tracking/
├── BACK-END/
│   └── time-tracking/              # Projeto Maven-Spring Boot
│       ├── src/
│       │   ├── main/
│       │   │   ├── java/          # Código Java
│       │   │   └── resources/     # application.properties
│       │   └── test/              # Testes
│       ├── target/                # Compilado (ignorado)
│       ├── pom.xml                # Dependências
│       └── mvnw                   # Maven Wrapper
│
├── FRONT-END/                      # Código web
│   ├── *.html                      # Páginas
│   ├── scripts/                    # JavaScript
│   └── styles/                     # CSS
│
├── README.md                        # Documentação principal
├── API_DOCUMENTATION.md            # Endpoints da API
├── QUICK_START.md                  # Este arquivo
└── .gitignore                      # Arquivos ignorados
```

---

## 🧪 Testando Localmente

### Teste 1: Health Check

```bash
curl http://localhost:8080/api/health
```

**Esperado:**
```json
{"status":"UP"}
```

---

### Teste 2: Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"cpf":"12345678900","password":"admin123"}'
```

**Esperado:**
```json
{
  "token": "eyJ...",
  "cpf": "12345678900",
  "name": "Admin",
  "role": "ADMIN"
}
```

---

### Teste 3: Criar Registro

```bash
curl -X POST http://localhost:8080/api/employee/time-records \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json"
```

---

## 🎨 Customizações Comuns

### Alterar Porta do Back-End

Arquivo: `BACK-END/time-tracking/src/main/resources/application.properties`

```properties
server.port=9000
```

---

### Alterar Credenciais Admin Padrão

Arquivo: `BACK-END/time-tracking/src/main/resources/application.properties`

```properties
admin.cpf=10987654321
admin.password=minha-senha-segura
```

---

### Alterar URL da API no Frontend

Arquivo: `FRONT-END/scripts/api.js`

```javascript
const API_BASE_URL = 'http://seu-servidor:8080/api';
```

---

### Alterar Cores do Frontend

Arquivo: `FRONT-END/styles/style.css`

```css
:root {
    --primary: #2563eb;           /* Azul principal */
    --secondary: #10b981;          /* Verde */
    --accent: #f59e0b;             /* Amarelo */
    --danger: #ef4444;             /* Vermelho */
}
```

---

## 📦 Dependências Principais

### Back-End
- **Spring Boot 4.0.3** - Framework
- **Spring Security** - Autenticação
- **Spring Data JPA** - BD
- **JWT (JJWT 0.11.5)** - Tokens
- **Lombok** - Less Boilerplate
- **MySQL** - Banco

### Front-End
- **HTML5** - Estrutura
- **CSS3** - Styling
- **JavaScript (Vanilla)** - Lógica
- **Font Awesome 6** - Ícones

---

## 🚀 Deploy em Produção

### Preparar para Produção

1. **Build Release:**
```bash
mvn clean package
```

2. **Configurar production properties:**
```properties
spring.jpa.hibernate.ddl-auto=validate
logging.level.root=WARN
```

3. **Usar Secret Management:**
- Armazene `jwt.secret` em variáveis de ambiente
- Use credenciais seguras para Banco de Dados

4. **Deploy JAR:**
```bash
java -jar time-tracking-0.0.1-SNAPSHOT.jar
```

---

## 📚 Documentação Adicional

- [README.md](README.md) - Documentação completa
- [API_DOCUMENTATION.md](API_DOCUMENTATION.md) - Endpoints da API
- [Spring Boot Docs](https://spring.io/projects/spring-boot)

---

## ❓ Respostas Rápidas

**P: Como adicionar novo usuário?**
R: Via Admin Dashboard → "Registrar Funcionário"

**P: Como resetar senha?**
R: Atualmente não há. Admin deve excluir e recriar o usuário.

**P: Posso acessar de outro computador?**
R: Sim, acesse: `http://IP-DA-MAQUINA:porta`

**P: Como fazer backup do banco?**
R: `mysqldump -u root -p time_tracking_db > backup.sql`

**P: Qual é o formato de data?**
R: Sempre `DD/MM/YYYY` (ex: 15/01/2025)

---

**Pronto! Você está saindo com sucesso! 🎉**

Se tiver problemas, verifique a seção [Troubleshooting](#troubleshooting) acima.

