# 🔌 API Documentation - Time Tracking System

Documentação detalhada de todos os endpoints da API RESTful.

---

## Índice

- [Autenticação](#autenticação)
- [Funcionário - Registros de Ponto](#funcionário---registros-de-ponto)
- [Administrador - Gerenciamento de Funcionários](#administrador---gerenciamento-de-funcionários)
- [Códigos de Status HTTP](#códigos-de-status-http)
- [Exemplos de Erro](#exemplos-de-erro)
- [Coleção Postman](#coleção-postman)

---

## Autenticação

### Base URL
```
http://localhost:8080/api
```

### Headers Padrão
```http
Content-Type: application/json
Authorization: Bearer {token}
```

---

## 🔐 Autenticação

### Login

Autentica um usuário e retorna um JWT token.

**Endpoint:**
```http
POST /auth/login
```

**Headers:**
```http
Content-Type: application/json
```

**Request Body:**
```json
{
  "cpf": "12345678900",
  "password": "admin123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMjM0NTY3ODkwMCIsImlhdCI6MTUxNjIzOTAyMn0.8dC5eK3vJ9mN2xL4pQ7rS9t1vR2wX5zY8aB3cD6eF0gH1iJ4kM5lN6oP7qR8sT9u",
  "cpf": "12345678900",
  "name": "Admin",
  "role": "ADMIN"
}
```

**Variáveis:**
| Campo | Tipo | Descrição |
|-------|------|-----------|
| `token` | string | JWT token para autenticação |
| `cpf` | string | CPF do usuário |
| `name` | string | Nome completo |
| `role` | string | Papel (EMPLOYEE ou ADMIN) |

**Exemplos de Erro:**

- **400 Bad Request** - CPF ou senha inválidos
```json
{
  "error": "CPF ou senha inválidos",
  "status": 400
}
```

- **401 Unauthorized** - Credenciais incorretas
```json
{
  "error": "Unauthorized",
  "status": 401
}
```

**cURL:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "cpf": "12345678900",
    "password": "admin123"
  }'
```

---

## 👤 Funcionário - Registros de Ponto

### 1. Criar Registro de Ponto

Cria um novo registro de ponto para o funcionário autenticado.

**Endpoint:**
```http
POST /employee/time-records
```

**Headers:**
```http
Authorization: Bearer {token}
Content-Type: application/json
```

**Request Body:**
```json
{}
```

*Nota: Sem corpo obrigatório, usa informações do usuário autenticado*

**Response (201 Created):**
```json
{
  "id": 1,
  "employeeName": "João Silva",
  "employeePis": "12345678901",
  "companyName": "Tech Solutions Ltda",
  "companyCnpj": "12.345.678/0001-90",
  "recordDate": "16/01/2025",
  "recordTime": "08:30:45",
  "dayOfWeek": "WEDNESDAY"
}
```

**cURL:**
```bash
curl -X POST http://localhost:8080/api/employee/time-records \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json"
```

---

### 2. Listar Registros de Ponto

Lista todos os registros de ponto do funcionário autenticado com paginação.

**Endpoint:**
```http
GET /employee/time-records?page=0&size=10
```

**Query Parameters:**
| Parâmetro | Tipo | Padrão | Descrição |
|-----------|------|--------|-----------|
| `page` | integer | 0 | Número da página (começa em 0) |
| `size` | integer | 10 | Quantidade de registros por página |

**Headers:**
```http
Authorization: Bearer {token}
```

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "employeeName": "João Silva",
      "employeePis": "12345678901",
      "companyName": "Tech Solutions Ltda",
      "companyCnpj": "12.345.678/0001-90",
      "recordDate": "16/01/2025",
      "recordTime": "08:30:45",
      "dayOfWeek": "WEDNESDAY"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "totalElements": 1,
    "totalPages": 1
  }
}
```

**cURL:**
```bash
curl -X GET "http://localhost:8080/api/employee/time-records?page=0&size=10" \
  -H "Authorization: Bearer {token}"
```

---

### 3. Buscar Registros por Data

Busca registros de ponto de uma data específica.

**Endpoint:**
```http
GET /employee/time-records/search?date=15/01/2025&page=0&size=10
```

**Query Parameters:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| `date` | string (DD/MM/YYYY) | Data para filtrar |
| `page` | integer | Número da página |
| `size` | integer | Quantidade por página |

**Headers:**
```http
Authorization: Bearer {token}
```

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "employeeName": "João Silva",
      "employeePis": "12345678901",
      "companyName": "Tech Solutions Ltda",
      "companyCnpj": "12.345.678/0001-90",
      "recordDate": "15/01/2025",
      "recordTime": "14:35:20",
      "dayOfWeek": "TUESDAY"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "totalElements": 1,
    "totalPages": 1
  }
}
```

**cURL:**
```bash
curl -X GET "http://localhost:8080/api/employee/time-records/search?date=15/01/2025&page=0&size=10" \
  -H "Authorization: Bearer {token}"
```

---

### 4. Registros de Hoje

Lista apenas os registros do dia atual.

**Endpoint:**
```http
GET /employee/time-records/today?page=0&size=10
```

**Query Parameters:**
| Parâmetro | Tipo | Padrão |
|-----------|------|--------|
| `page` | integer | 0 |
| `size` | integer | 10 |

**Headers:**
```http
Authorization: Bearer {token}
```

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": 15,
      "employeeName": "João Silva",
      "employeePis": "12345678901",
      "companyName": "Tech Solutions Ltda",
      "companyCnpj": "12.345.678/0001-90",
      "recordDate": "16/01/2025",
      "recordTime": "17:30:00",
      "dayOfWeek": "WEDNESDAY"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "totalElements": 1,
    "totalPages": 1
  }
}
```

**cURL:**
```bash
curl -X GET "http://localhost:8080/api/employee/time-records/today?page=0&size=10" \
  -H "Authorization: Bearer {token}"
```

---

### 5. Último Registro

Retorna o registro de ponto mais recente do funcionário.

**Endpoint:**
```http
GET /employee/time-records/latest
```

**Headers:**
```http
Authorization: Bearer {token}
```

**Response (200 OK):**
```json
{
  "id": 15,
  "employeeName": "João Silva",
  "employeePis": "12345678901",
  "companyName": "Tech Solutions Ltda",
  "companyCnpj": "12.345.678/0001-90",
  "recordDate": "16/01/2025",
  "recordTime": "17:30:00",
  "dayOfWeek": "WEDNESDAY"
}
```

**Response (204 No Content):**
```
Sem registros de ponto ainda
```

**cURL:**
```bash
curl -X GET "http://localhost:8080/api/employee/time-records/latest" \
  -H "Authorization: Bearer {token}"
```

---

## 👨‍💼 Administrador - Gerenciamento de Funcionários

> ⚠️ **Requer autenticação como ADMIN**

### 1. Registrar Novo Funcionário

Cria um novo funcionário no sistema.

**Endpoint:**
```http
POST /admin/employees
```

**Headers:**
```http
Authorization: Bearer {admin-token}
Content-Type: application/json
```

**Request Body:**
```json
{
  "cpf": "98765432100",
  "name": "Maria Santos",
  "pis": "98765432101",
  "password": "senha123",
  "companyName": "Tech Solutions Ltda",
  "companyCnpj": "12.345.678/0001-90"
}
```

**Campos Obrigatórios:**
| Campo | Tipo | Validação |
|-------|------|-----------|
| `cpf` | string | 11 dígitos, único |
| `name` | string | Máx 100 caracteres |
| `pis` | string | Máx 20 caracteres, único |
| `password` | string | Mínimo 6 caracteres |
| `companyName` | string | Máx 100 caracteres |
| `companyCnpj` | string | Formato CNPJ |

**Response (201 Created):**
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

**Exemplos de Erro:**

- **400 Bad Request** - Dados inválidos
```json
{
  "error": "CPF deve ter 11 dígitos",
  "status": 400
}
```

- **409 Conflict** - CPF ou PIS já existe
```json
{
  "error": "CPF já cadastrado no sistema",
  "status": 409
}
```

**cURL:**
```bash
curl -X POST http://localhost:8080/api/admin/employees \
  -H "Authorization: Bearer {admin-token}" \
  -H "Content-Type: application/json" \
  -d '{
    "cpf": "98765432100",
    "name": "Maria Santos",
    "pis": "98765432101",
    "password": "senha123",
    "companyName": "Tech Solutions Ltda",
    "companyCnpj": "12.345.678/0001-90"
  }'
```

---

### 2. Listar Todos os Funcionários

Lista todos os funcionários com paginação.

**Endpoint:**
```http
GET /admin/employees?page=0&size=10
```

**Query Parameters:**
| Parâmetro | Tipo | Padrão | Descrição |
|-----------|------|--------|-----------|
| `page` | integer | 0 | Número da página |
| `size` | integer | 10 | Registros por página |

**Headers:**
```http
Authorization: Bearer {admin-token}
```

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "cpf": "12345678900",
      "name": "Admin",
      "pis": "12345678901",
      "companyName": "Tech Solutions Ltda",
      "companyCnpj": "12.345.678/0001-90",
      "role": "ADMIN",
      "enabled": true
    },
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
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "totalElements": 2,
    "totalPages": 1
  }
}
```

**cURL:**
```bash
curl -X GET "http://localhost:8080/api/admin/employees?page=0&size=10" \
  -H "Authorization: Bearer {admin-token}"
```

---

### 3. Obter Funcionário por CPF

Retorna informações de um funcionário específico.

**Endpoint:**
```http
GET /admin/employees/{cpf}
```

**Path Parameters:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| `cpf` | string | CPF do funcionário |

**Headers:**
```http
Authorization: Bearer {admin-token}
```

**Response (200 OK):**
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

**Response (404 Not Found):**
```json
{
  "error": "Funcionário não encontrado",
  "status": 404
}
```

**cURL:**
```bash
curl -X GET http://localhost:8080/api/admin/employees/98765432100 \
  -H "Authorization: Bearer {admin-token}"
```

---

### 4. Atualizar Funcionário

Atualiza informações de um funcionário existente.

**Endpoint:**
```http
PUT /admin/employees/{cpf}
```

**Path Parameters:**
| Parâmetro | Tipo |
|-----------|------|
| `cpf` | string |

**Headers:**
```http
Authorization: Bearer {admin-token}
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "Maria Silva",
  "pis": "98765432102",
  "companyName": "Tech Solutions Ltda",
  "companyCnpj": "12.345.678/0001-90"
}
```

*Nota: CPF não pode ser alterado (é identificador único)*

**Response (200 OK):**
```json
{
  "id": 2,
  "cpf": "98765432100",
  "name": "Maria Silva",
  "pis": "98765432102",
  "companyName": "Tech Solutions Ltda",
  "companyCnpj": "12.345.678/0001-90",
  "role": "EMPLOYEE",
  "enabled": true
}
```

**cURL:**
```bash
curl -X PUT http://localhost:8080/api/admin/employees/98765432100 \
  -H "Authorization: Bearer {admin-token}" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Maria Silva",
    "pis": "98765432102",
    "companyName": "Tech Solutions Ltda",
    "companyCnpj": "12.345.678/0001-90"
  }'
```

---

### 5. Deletar Funcionário

Remove um funcionário do sistema.

**Endpoint:**
```http
DELETE /admin/employees/{cpf}
```

**Path Parameters:**
| Parâmetro | Tipo |
|-----------|------|
| `cpf` | string |

**Headers:**
```http
Authorization: Bearer {admin-token}
```

**Response (204 No Content):**
```
(sem corpo)
```

**Response (404 Not Found):**
```json
{
  "error": "Funcionário não encontrado",
  "status": 404
}
```

**cURL:**
```bash
curl -X DELETE http://localhost:8080/api/admin/employees/98765432100 \
  -H "Authorization: Bearer {admin-token}"
```

---

### 6. Registros de Ponto de Funcionário

Lista os registros de ponto de um funcionário específico.

**Endpoint:**
```http
GET /admin/employees/{cpf}/time-records?page=0&size=10
```

**Path Parameters:**
| Parâmetro | Tipo |
|-----------|------|
| `cpf` | string |

**Query Parameters:**
| Parâmetro | Tipo | Padrão |
|-----------|------|--------|
| `page` | integer | 0 |
| `size` | integer | 10 |

**Headers:**
```http
Authorization: Bearer {admin-token}
```

**Response (200 OK):**
```json
{
  "content": [
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
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "totalElements": 1,
    "totalPages": 1
  }
}
```

**cURL:**
```bash
curl -X GET "http://localhost:8080/api/admin/employees/98765432100/time-records?page=0&size=10" \
  -H "Authorization: Bearer {admin-token}"
```

---

## 📊 Códigos de Status HTTP

| Código | Significado | Descrição |
|--------|------------|-----------|
| 200 | OK | Requisição bem-sucedida |
| 201 | Created | Recurso criado com sucesso |
| 204 | No Content | Requisição bem-sucedida sem conteúdo |
| 400 | Bad Request | Dados de entrada inválidos |
| 401 | Unauthorized | Sem autenticação ou token inválido |
| 403 | Forbidden | Sem permissão para acessar o recurso |
| 404 | Not Found | Recurso não encontrado |
| 409 | Conflict | Conflito (ex: CPF duplicado) |
| 500 | Internal Server Error | Erro no servidor |

---

## 🚨 Exemplos de Erro

### Erro de Validação

```json
{
  "timestamp": "2025-01-16T08:30:00.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Falha na validação dos dados",
  "errors": [
    {
      "field": "cpf",
      "message": "CPF deve conter 11 dígitos"
    },
    {
      "field": "password",
      "message": "Senha deve ter no mínimo 6 caracteres"
    }
  ]
}
```

### Erro de Autenticação

```json
{
  "timestamp": "2025-01-16T08:30:00.000+00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Token inválido ou expirado"
}
```

### Erro de Permissão

```json
{
  "timestamp": "2025-01-16T08:30:00.000+00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Você não tem permissão para acessar este recurso"
}
```

### Erro de Recurso Não Encontrado

```json
{
  "timestamp": "2025-01-16T08:30:00.000+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Funcionário não encontrado com CPF: 12345678900"
}
```

---

## 📮 Coleção Postman

### Requisitos
- [Postman](https://www.postman.com/downloads/) instalado

### Importar Coleção

1. Abra Postman
2. Clique em "Import" → "Link"
3. Cole: `link-para-sua-colecao-postman`
4. Clique em "Import"

### Configurar Variáveis

1. No Postman, vá para "Environments" → "New"
2. Crie variáveis:
   - `base_url`: `http://localhost:8080/api`
   - `admin_cpf`: `12345678900`
   - `admin_password`: `admin123`
   - `token`: (será preenchido automaticamente após login)
   - `employee_cpf`: (CPF de um funcionário)

3. Salve o environment

### Testar Collection

1. Selecione o environment criado
2. Execute os testes em sequência:
   - Login (Admin)
   - Create Employee
   - List Employees
   - Get Employee
   - Update Employee
   - Create Time Record
   - List Time Records
   - Search by Date
   - Delete Employee

Cada teste preencherá as variáveis automaticamente para o próximo.

---

## 💡 Dicas de Uso

### Usando Bearer Token

Todas as requisições protegidas devem incluir o header:
```http
Authorization: Bearer {token}
```

Exemplo com cURL:
```bash
curl -X GET http://localhost:8080/api/admin/employees \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

### Manipulando Paginação

Para usar paginação:
```
GET /api/employee/time-records?page=0&size=10
```
- `page=0` - primeira página
- `size=10` - 10 registros por página
- `page=1&size=5` - 5 registros na segunda página

### Formatação de Data

Use sempre o formato: **DD/MM/YYYY**
- Correto: `15/01/2025`
- Incorreto: `2025-01-15`

### Formatação de CPF

Use 11 dígitos sem pontuação:
- Correto: `12345678900`
- Incorreto: `123.456.789-00`

---

## 📚 Referências

- [REST API Best Practices](https://restfulapi.net/)
- [HTTP Status Codes](https://httpwg.org/specs/rfc7231.html#status.codes)
- [JWT.io](https://jwt.io/)
- [Postman Documentation](https://learning.postman.com/)

