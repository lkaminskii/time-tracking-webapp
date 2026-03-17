# 🕐 Sistema de Registro de Ponto

Uma plataforma web corporativa intuitiva para gerenciamento eficiente do registro de ponto de funcionários.

---

## 🎯 Visão Geral

O **Sistema de Registro de Ponto** é uma aplicação web corporativa completa desenvolvida com **Spring Boot** no back-end e **HTML5/JavaScript** no front-end. A plataforma permite que funcionários registrem seus horários de entrada e saída, enquanto administradores possam gerenciar colaboradores, visualizar relatórios e acompanhar registros de ponto.

**Principais características:**
- ✅ Autenticação segura com JWT
- ✅ Controle baseado em papéis (EMPLOYEE e ADMIN)
- ✅ Registro de ponto com data, hora e dia da semana
- ✅ Dashboard intuitivo com histórico de registros
- ✅ Gerenciamento completo de funcionários
- ✅ Persistência em banco de dados MySQL
- ✅ Interface responsiva e moderna
- ✅ Suporte a múltiplos idiomas (Português)

---

## 🛠️ Tecnologias Utilizadas

### Back-End
| Tecnologia | Versão | Descrição |
|-----------|--------|-----------|
| **Java** | 17 | Linguagem de programação |
| **Spring Boot** | 4.0.3 | Framework para aplicações Java |
| **Spring Security** | 6.0+ | Autenticação e autorização |
| **Spring Data JPA** | - | Persistência de dados |
| **JWT (JJWT)** | 0.11.5 | Tokens de autenticação |
| **MySQL Connector** | - | Driver para banco de dados |
| **Lombok** | - | Redução de código boilerplate |
| **Maven** | 3.8+ | Gerenciador de dependências |

### Front-End
| Tecnologia | Descrição |
|-----------|-----------|
| **HTML5** | Estrutura das páginas |
| **CSS3** | Estilização com variáveis CSS |
| **JavaScript (Vanilla)** | Lógica da aplicação |
| **Font Awesome 6** | Ícones |
| **LocalStorage** | Armazenamento de tokens e dados do usuário |

### Banco de Dados
| Tecnologia | Descrição |
|-----------|-----------|
| **MySQL** | Banco de dados relacional |
| **Hibernate** | ORM para mapeamento objeto-relacional |

---

## 🏗️ Arquitetura

O sistema segue uma arquitetura **cliente-servidor** com separação clara de responsabilidades:

```
┌─────────────────────────────────────────────────────────────┐
│                      CLIENTE (Front-End)                    │
│  ┌──────────────────────────────────────────────────────┐   │
│  │ HTML + CSS + JavaScript (Vanilla)                   │   │
│  │ - login.html (Autenticação)                         │   │
│  │ - dashboard.html (Painel principal)                 │   │
│  │ - Requisições HTTP com JWT Bearer Token             │   │
│  └──────────────────────────────────────────────────────┘   │
└──────────────────────────┬──────────────────────────────────┘
                           │ HTTP REST
                           ↓
┌──────────────────────────────────────────────────────────────┐
│                  SERVIDOR (Back-End)                         │
│  ┌──────────────────────────────────────────────────────┐   │
│  │ Spring Boot Application                              │   │
│  │                                                      │   │
│  │ Controllers → Services → Repositories → Models      │   │
│  │ - AuthController                                    │   │
│  │ - TimeRecordController                              │   │
│  │ - AdminController                                   │   │
│  │                                                      │   │
│  │ Security Layer                                       │   │
│  │ - JWT Authentication Filter                         │   │
│  │ - Role-Based Access Control                         │   │
│  └──────────────────────────────────────────────────────┘   │
└──────────────────────────┬──────────────────────────────────┘
                           │ JDBC/JPA
                           ↓
┌──────────────────────────────────────────────────────────────┐
│                   BANCO DE DADOS (MySQL)                     │
│  - employees (Funcionários)                                  │
│  - time_records (Registros de ponto)                         │
└──────────────────────────────────────────────────────────────┘
```

---

## ✨ Funcionalidades

### 👤 Funcionalidades de Funcionário (EMPLOYEE)
- **Autenticação**: Login seguro com CPF e senha
- **Registrar Ponto**: Criar registros de entrada/saída com data, hora e dia da semana
- **Visualizar Histórico**: Consultar todos os registros de ponto
- **Buscar por Data**: Filtrar registros por data específica
- **Ver Registros Hoje**: Visualizar registros do dia atual
- **Consultar Último Registro**: Verificar o último ponto registrado

### 👨‍💼 Funcionalidades de Administrador (ADMIN)
- **Autenticação Admin**: Login como administrador usando CPF e senha padrão
- **Gerenciar Funcionários**:
  - ✏️ Registrar novos funcionários
  - 👁️ Visualizar informações detalhadas
  - ✏️ Atualizar dados de funcionários
  - 🗑️ Remover funcionários
  - 📋 Listar todos os funcionários (com paginação)
- **Acompanhar Registros**: Visualizar histórico de ponto de qualquer funcionário
- **Dashboard Administrativo**: Estatísticas e relatórios
- **Auditoria**: Registros de todas as operações realizadas

### 🔒 Segurança
- Autenticação via **JWT (JSON Web Tokens)**
- Criptografia de senhas
- Validação de dados de entrada
- CORS configurado
- Filtros de autenticação por papéis (roles)

---

## 📁 Estrutura do Projeto

### Back-End

```
BACK-END/time-tracking/
│
├── src/main/java/dev/lucas/time_tracking/
│   ├── TimeTrackingApplication.java          # Classe principal da aplicação
│   │
│   ├── controller/                          # Controladores REST
│   │   ├── AuthController.java              # Autenticação (login)
│   │   ├── TimeRecordController.java        # Gerenciar registros de ponto
│   │   ├── AdminController.java             # Operações administrativas
│   │   └── HealthCheckController.java       # Verificação de saúde
│   │
│   ├── service/                             # Lógica de negócio
│   │   ├── AuthenticationService.java       # Autenticação
│   │   ├── EmployeeService.java             # Operações com funcionários
│   │   ├── TimeRecordService.java           # Operações com registros
│   │   ├── AdminService.java                # Operações administrativas
│   │   └── impl/                            # Implementações dos serviços
│   │       ├── AuthenticationServiceImpl.java
│   │       ├── EmployeeServiceImpl.java
│   │       ├── TimeRecordServiceImpl.java
│   │       └── AdminServiceImpl.java
│   │
│   ├── model/                               # Entidades JPA
│   │   ├── Employee.java                   # Modelo de Funcionário
│   │   ├── TimeRecord.java                 # Modelo de Registro de Ponto
│   │   └── Role.java                       # Enum de Papéis
│   │
│   ├── repository/                          # Acesso a dados
│   │   ├── EmployeeRepository.java         # Repositório de Funcionários
│   │   ├── TimeRecordRepository.java       # Repositório de Registros
│   │
│   ├── dto/                                 # Data Transfer Objects
│   │   ├── LoginRequest.java               # Requisição de login
│   │   ├── JwtAuthenticationResponse.java  # Resposta de autenticação
│   │   ├── EmployeeRegistrationRequest.java # Registro de funcionário
│   │   ├── EmployeeResponse.java           # Resposta de funcionário
│   │   ├── TimeRecordResponse.java         # Resposta de registro
│   │   ├── DashboardStats.java             # Estatísticas do dashboard
│   │   └── ErrorResponse.java              # Resposta de erro
│   │
│   ├── security/                            # Segurança e autenticação
│   │   ├── JwtTokenProvider.java           # Processamento de JWT
│   │   ├── JwtAuthenticationFilter.java    # Filtro de autenticação
│   │   ├── JwtAuthenticationEntryPoint.java # Tratamento de erros
│   │   ├── CustomUserDetailsService.java   # Carregamento de usuários
│   │   └── UserPrincipal.java              # Principal do usuário autenticado
│   │
│   ├── config/                              # Configurações
│   │   └── SecurityConfig.java             # Configuração de segurança
│   │
│   └── exception/                           # Tratamento de exceções customizado
│
├── src/main/resources/
│   └── application.properties               # Configurações da aplicação
│
├── src/test/java/                          # Testes unitários e de integração
│   └── dev/lucas/time_tracking/
│       ├── controller/
│       ├── service/
│       ├── security/
│       ├── dto/
│       ├── model/
│       └── TimeTrackingApplicationTests.java
│
├── pom.xml                                  # Arquivo de configuração Maven
├── mvnw / mvnw.cmd                         # Maven Wrapper
└── HELP.md                                  # Documentação de ajuda
```

### Front-End

```
FRONT-END/
│
├── index.html                       # Página de redirecionamento
├── login.html                       # Página de autenticação
├── dashboard.html                   # Dashboard principal
│
├── scripts/
│   ├── api.js                      # Funções de requisição HTTP
│   ├── auth.js                     # Lógica de autenticação
│   └── dashboard.js                # Lógica do dashboard
│
└── styles/
    └── style.css                   # Estilos CSS com variáveis
```

---

## 💻 Requisitos do Sistema

### Requisitos Mínimos
- **Java Development Kit (JDK)** 17 ou superior
- **MySQL Server** 5.7 ou superior
- **Maven** 3.8+ (ou use o Maven Wrapper incluído)
- **Navegador Web** moderno (Chrome, Firefox, Safari, Edge)
- **Node.js** (opcional, apenas se necessário para ferramentas adicionais)

### Requisitos Recomendados
- **IDE**: IntelliJ IDEA, VS Code, ou Eclipse
- **MySQL Workbench** (para gerenciar o banco de dados)
- **Postman** (para testar API endpoints)
- **Git** para controle de versão

---

## 🚀 Instalação e Configuração

### Passo 1: Clonar o Repositório

```bash
git clone https://github.com/seu-usuario/time-tracking.git
cd time-tracking
```

### Passo 2: Configurar o Banco de Dados MySQL

```bash
# Abra o MySQL Command Line Client
mysql -u root -p

# Execute os seguintes comandos
CREATE DATABASE time_tracking_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'root'@'localhost' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON time_tracking_db.* TO 'root'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

### Passo 3: Configurar Variáveis de Ambiente

Edite o arquivo `BACK-END/time-tracking/src/main/resources/application.properties`:

```properties
# Configuração do Banco de Dados
spring.datasource.url=jdbc:mysql://localhost:3306/time_tracking_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Configuração Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.properties.hibernate.format_sql=true

# Credenciais do Administrador Padrão
admin.cpf=12345678900
admin.password=admin123

# Configuração JWT
jwt.secret=mySecretKeyForJWTTokenGenerationAndValidation2024
jwt.expiration=86400000
jwt.refresh-expiration=604800000

# Nível de Log
logging.level.org.springframework.security=DEBUG
logging.level.com.time-tracking=DEBUG
```

### Passo 4: Instalar Dependências

```bash
# Navegar para o diretório do back-end
cd BACK-END/time-tracking

# Instalar dependências com Maven
mvn clean install

# Ou usar o Maven Wrapper (não requer Maven instalado)
./mvnw clean install
```

---

## 🔧 Variáveis de Ambiente

| Variável | Valor Padrão | Descrição |
|----------|--------------|-----------|
| `spring.datasource.url` | `jdbc:mysql://localhost:3306/time_tracking_db` | URL do banco de dados |
| `spring.datasource.username` | `root` | Usuário do MySQL |
| `spring.datasource.password` | `root` | Senha do MySQL |
| `admin.cpf` | `12345678900` | CPF do administrador padrão |
| `admin.password` | `admin123` | Senha do administrador padrão |
| `jwt.secret` | `mySecretKeyForJWTTokenGenerationAndValidation2024` | Chave secreta para assinar JWT |
| `jwt.expiration` | `86400000` | Expiração do token (24h em ms) |
| `server.port` | `8080` | Porta do servidor |

> **⚠️ IMPORTANTE**: Em produção, altere as senhas padrão e use variáveis de ambiente seguras!

---

## ▶️ Executando o Projeto

### Back-End

**Opção 1: Usando Maven Wrapper (Recomendado)**
```bash
cd BACK-END/time-tracking
./mvnw spring-boot:run
```

**Opção 2: Usando Maven instalado globalmente**
```bash
cd BACK-END/time-tracking
mvn spring-boot:run
```

**Opção 3: Executar arquivo JAR construído**
```bash
cd BACK-END/time-tracking
mvn clean package
java -jar target/time-tracking-0.0.1-SNAPSHOT.jar
```

O servidor iniciará em: **http://localhost:8080**

### Front-End

**Opção 1: Usando Live Server (VS Code Extension)**
- Instale a extensão "Live Server" no VS Code
- Clique com o botão direito em `FRONT-END/index.html`
- Selecione "Open with Live Server"
- A aplicação abrirá em: **http://localhost:5500**

**Opção 2: Usando Python (se instalado)**
```bash
cd FRONT-END
python -m http.server 8000
# Acesse: http://localhost:8000
```

**Opção 3: Usando Node.js (se instalado)**
```bash
cd FRONT-END
npx http-server -p 8000
# Acesse: http://localhost:8000
```

### Testes de Funcionamento

1. **Abra** a aplicação: http://localhost:8000 (ou porta configurada)
2. **Faça login** como administrador:
   - CPF: `12345678900`
   - Senha: `admin123`
3. **Navegue** pelos menus disponíveis

---

## 📊 Modelos de Dados

### Employee (Funcionário)

```java
@Entity
@Table(name = "employees")
public class Employee implements UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 11)
    private String cpf;              // Identificador único
    
    @Column(nullable = false, length = 100)
    private String name;              // Nome completo
    
    @Column(unique = true, nullable = false, length = 20)
    private String pis;              // PIS/PASEP
    
    @Column(nullable = false)
    private String password;          // Senha criptografada
    
    @Column(name = "company_name", nullable = false, length = 100)
    private String companyName;      // Nome da empresa
    
    @Column(name = "company_cnpj", nullable = false, length = 18)
    private String companyCnpj;      // CNPJ da empresa
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;                // EMPLOYEE ou ADMIN
    
    @Column(nullable = false)
    private boolean enabled;          // Status ativo/inativo
}
```

**Relacionamentos:**
- Um Funcionário pode ter muitos Registros de Ponto (1:N)

---

### TimeRecord (Registro de Ponto)

```java
@Entity
@Table(name = "time_records")
public class TimeRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;       // Referência ao funcionário
    
    @Column(name = "employee_name", nullable = false, length = 100)
    private String employeeName;     // Nome do funcionário
    
    @Column(name = "employee_pis", nullable = false, length = 20)
    private String employeePis;      // PIS do funcionário
    
    @Column(name = "company_name", nullable = false, length = 100)
    private String companyName;      // Nome da empresa
    
    @Column(name = "company_cnpj", nullable = false, length = 18)
    private String companyCnpj;      // CNPJ da empresa
    
    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;    // Data do registro (DD/MM/YYYY)
    
    @Column(name = "record_time", nullable = false)
    private LocalTime recordTime;    // Hora do registro (HH:MM:SS)
    
    @Column(name = "day_of_week", nullable = false, length = 20)
    private String dayOfWeek;        // Dia da semana
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;  // Data/hora de criação
}
```

**Relacionamentos:**
- Muitos Registros pertencem a Um Funcionário (N:1)

---

### Role (Papel)

```java
public enum Role {
    EMPLOYEE,    // Funcionário comum
    ADMIN        // Administrador
}
```

---

## 🔌 API Endpoints

### Autenticação

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "cpf": "12345678900",
  "password": "admin123"
}
```

**Resposta (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "cpf": "12345678900",
  "name": "Admin",
  "role": "ADMIN"
}
```

---

### Registros de Ponto (Funcionário)

#### Criar Registro
```http
POST /api/employee/time-records
Authorization: Bearer {token}
```

**Resposta (201 Created):**
```json
{
  "id": 1,
  "employeeName": "João Silva",
  "employeePis": "12345678901",
  "companyName": "Tech Solutions Ltda",
  "companyCnpj": "12.345.678/0001-90",
  "recordDate": "15/01/2025",
  "recordTime": "08:30:45",
  "dayOfWeek": "TUESDAY"
}
```

#### Listar Registros
```http
GET /api/employee/time-records?page=0&size=10
Authorization: Bearer {token}
```

#### Buscar por Data
```http
GET /api/employee/time-records/search?date=15/01/2025&page=0&size=10
Authorization: Bearer {token}
```

#### Registros de Hoje
```http
GET /api/employee/time-records/today?page=0&size=10
Authorization: Bearer {token}
```

#### Último Registro
```http
GET /api/employee/time-records/latest
Authorization: Bearer {token}
```

---

### Administração de Funcionários (Apenas ADMIN)

#### Registrar Novo Funcionário
```http
POST /api/admin/employees
Authorization: Bearer {admin-token}
Content-Type: application/json

{
  "cpf": "98765432100",
  "name": "Maria Santos",
  "pis": "98765432101",
  "password": "senha123",
  "companyName": "Tech Solutions Ltda",
  "companyCnpj": "12.345.678/0001-90"
}
```

**Resposta (201 Created):**
```json
{
  "id": 2,
  "cpf": "98765432100",
  "name": "Maria Santos",
  "pis": "98765432101",
  "companyName": "Tech Solutions Ltda",
  "companyCnpj": "12.345.678/0001-90",
  "role": "EMPLOYEE",
  "enabled": true
}
```

#### Listar Todos os Funcionários
```http
GET /api/admin/employees?page=0&size=10
Authorization: Bearer {admin-token}
```

#### Obter Funcionário por CPF
```http
GET /api/admin/employees/{cpf}
Authorization: Bearer {admin-token}
```

#### Atualizar Funcionário
```http
PUT /api/admin/employees/{cpf}
Authorization: Bearer {admin-token}
Content-Type: application/json

{
  "name": "Maria Silva",
  "pis": "98765432102",
  "companyName": "Tech Solutions Ltda",
  "companyCnpj": "12.345.678/0001-90"
}
```

#### Deletar Funcionário
```http
DELETE /api/admin/employees/{cpf}
Authorization: Bearer {admin-token}
```

#### Registros de Ponto de Funcionário
```http
GET /api/admin/employees/{cpf}/time-records?page=0&size=10
Authorization: Bearer {admin-token}
```

---

## 🔐 Autenticação e Segurança

### Fluxo de Autenticação

```
1. Usuário submete credenciais (CPF + Senha)
                ↓
2. AuthController recebe a requisição
                ↓
3. AuthenticationService valida as credenciais
                ↓
4. CustomUserDetailsService carrega dados do usuário
                ↓
5. JwtTokenProvider gera um JWT Token
                ↓
6. Token é retornado ao cliente
                ↓
7. Cliente armazena no LocalStorage
                ↓
8. Requisições subsequentes incluem: Authorization: Bearer {token}
                ↓
9. JwtAuthenticationFilter intercepta requisições e valida o token
                ↓
10. Requisição é processada se token é válido
```

### Configuração de Segurança

- **Autenticação**: JWT (JSON Web Tokens)
- **Criptografia**: BCrypt para senhas
- **Validação**: Filtros de autenticação em todas as rotas protegidas
- **CORS**: Configurado para aceitar requisições cross-origin
- **Expiração**: Tokens expiram em 24 horas

### Headers de Autenticação

```http
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMjM0NTY3ODkwMCIsImlhdCI6MTUxNjIzOTAyMn0.signature
```

---

## 📱 Como Usar

### Para Funcionários

1. **Acessar a Aplicação**
   - Abra: http://localhost:8000 (ajuste porta conforme necessário)

2. **Fazer Login**
   - Insira seu CPF (11 dígitos)
   - Insira sua senha
   - Clique em "Entrar"

3. **Dashboard**
   - Visualize seu histórico de registros
   - Clique no botão de "Registrar Ponto" para marcar entrada/saída
   - Use filtros para buscar registros por data

4. **Consultar Registros**
   - Veja todos os seus registros
   - Filtre por data específica
   - Consulte o último registro realizado

### Para Administradores

1. **Fazer Login como Admin**
   - CPF: `12345678900`
   - Senha: `admin123`
   - ⚠️ **ALTERE ESTAS CREDENCIAIS APÓS A PRIMEIRA EXECUÇÃO**

2. **Gerenciar Funcionários**
   - Vá para a seção "Administração"
   - Registre novos funcionários
   - Edite informações de funcionários existentes
   - Remova funcionários quando necessário

3. **Acompanhar Registros**
   - Visualize registros de qualquer funcionário
   - Exporte relatórios (funcionalidade futura)

4. **Dashboard Administrativo**
   - Veja estatísticas gerais
   - Total de funcionários
   - Total de registros de ponto
   - Atividades recentes

---

## ✅ Testes

### Testes Automatizados

O projeto inclui testes unitários e de integração das principais funcionalidades:

```bash
# Executar todos os testes
cd BACK-END/time-tracking
mvn test

# Executar testes de uma classe específica
mvn test -Dtest=AuthenticationServiceImplTest

# Executar com relatório de cobertura
mvn test jacoco:report
```

### Testes Disponíveis

- `EmployeeTest.java` - Testes do modelo Employee
- `TimeRecordTest.java` - Testes do modelo TimeRecord
- `AuthenticationServiceImplTest.java` - Autenticação
- `EmployeeServiceImplTest.java` - Operações com funcionários
- `TimeRecordServiceImplTest.java` - Operações com registros
- `AdminServiceImplTest.java` - Operações administrativas
- `JwtTokenProviderTest.java` - Geração e validação de JWT
- `JwtAuthenticationFilterTest.java` - Filtro de autenticação
- E mais...

### Testar com Postman

1. Importe a coleção de endpoints (arquivo `.postman_collection.json`)
2. Configure variáveis de ambiente (base_url, token)
3. Execute os testes em sequência

---

## 🤝 Contribuindo

Contribuições são bem-vindas! Para contribuir ao projeto:

1. **Faça um Fork** do repositório
2. **Crie uma Branch** para sua feature (`git checkout -b feature/AmazingFeature`)
3. **Commit suas Mudanças** (`git commit -m 'Add some AmazingFeature'`)
4. **Push para a Branch** (`git push origin feature/AmazingFeature`)
5. **Abra um Pull Request**

### Diretrizes de Contribuição

- Siga convenções de codificação Java e JavaScript
- Adicione testes para novas funcionalidades
- Atualize documentação conforme necessário
- Respeite o padrão de commits: `tipo: descrição`
  - Exemplos: `feat: adicionar autenticação`, `fix: corrigir login`

---

## 📝 Licença

Este projeto está licenciado sob a [MIT License](LICENSE) - veja o arquivo LICENSE para detalhes.

---

## 👨‍💻 Autor

**Lucas**
- GitHub: [@seu-usuario](https://github.com/seu-usuario)
- Email: seu.email@example.com

---

## 🆘 Suporte

Se encontrar problemas ou tiver dúvidas:

1. **Abra uma Issue** no repositório
2. **Consulte a documentação** do [Spring Boot](https://spring.io/projects/spring-boot)
3. **Verifique configurações** do banco de dados
4. **Limpe cache** do navegador (Ctrl+Shift+Delete)

---

## 📚 Recursos Úteis

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security Guide](https://spring.io/guides/gs/securing-web/)
- [JWT Tutorial](https://jwt.io)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [MDN Web Docs](https://developer.mozilla.org/)

---

**⭐ Se este projeto foi útil, considere deixar uma estrela no repositório!**

