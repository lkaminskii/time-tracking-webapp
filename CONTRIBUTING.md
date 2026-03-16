# 🤝 Contributing Guide - Time Tracking System

Obrigado por considerar contribuir com o Time Tracking System! Este guia ajudará você a entender nosso processo de contribuição.

---

## 📋 Índice

- [Código de Conduta](#código-de-conduta)
- [Como Começar](#como-começar)
- [Tipos de Contribuição](#tipos-de-contribuição)
- [Fluxo de Trabalho](#fluxo-de-trabalho)
- [Padrões de Código](#padrões-de-código)
- [Pull Request](#pull-request)
- [Comunicação](#comunicação)

---

## 📜 Código de Conduta

Todos os contribuidores devem aderir to nosso [Código de Conduta](CODE_OF_CONDUCT.md) (em breve).

Respeitamos e valorizamos a diversidade de pensamentos, experiências e perspectivas.

---

## 🚀 Como Começar

### 1. Setup Local

```bash
# Fork o repositório no GitHub
# Clone seu fork
git clone https://github.com/seu-usuario/time-tracking.git
cd time-tracking

# Adicione upstream
git remote add upstream https://github.com/lucas-username/time-tracking.git

# Instale dependências
cd BACK-END/time-tracking
mvn install
```

### 2. Criar Branch

```bash
# Atualize main
git fetch upstream
git checkout main
git merge upstream/main

# Crie branch para sua feature
git checkout -b feature/sua-feature-aqui
```

### 3. Fazer Mudanças

Faça seu trabalho, commit frequente e com mensagens claras.

### 4. Submeter Pull Request

Push para seu fork e abra um PR contra `main`.

---

## 💡 Tipos de Contribuição

### 🐛 Reportar Bugs

**Ao abrir uma issue de bug:**

1. Use o template de bug predefinido
2. Descreva o comportamento esperado vs. real
3. Inclua passos para reproduzir
4. Compartilhe logs/screenshots se possível
5. Especifique seu ambiente (OS, versão Java, etc.)

**Exemplo:**
```
Título: Login falha com CPF contendo letras

Descrição:
- Esperado: Campo rejeita letras
- Atual: Aceita e envia para API
- Passos: 1. Ir para login 2. Digitar "1234567890A" 3. Clicar Enter
- Logs: [cole aqui]
```

---

### ✨ Sugerir Features

**Ao sugerir uma feature:**

1. Use o template de feature
2. Explique o caso de uso
3. Descreva a solução esperada
4. Liste alternativas consideradas
5. Inclua exemplos/mockups se relevante

**Exemplo:**
```
Título: Adicionar autenticação com 2FA

Descrição:
Caso de uso: Melhorar segurança para admins

Solução:
- SMS com código 6 dígitos
- Geração após login bem-sucedido
- Validade de 5 minutos

Alternativas:
- Google Authenticator
- Email
```

---

### 📝 Documentação

**Melhorias documentação são bem-vindas!**

- Corrigir typos
- Esclarecer instruções confusas
- Adicionar exemplos
- Traduzir para outros idiomas

---

### 🎨 Code Review

- Revisar PRs abertas
- Sugerir melhorias
- Testar mudanças localmente
- Reportar issues

---

## 🔄 Fluxo de Trabalho

### 1. Identificar Problema

```bash
# Veja issues abertas
https://github.com/seu-usuario/time-tracking/issues
```

### 2. Criar Feature Branch

```bash
git checkout -b feature/nome-descritivo

# Existem 4 tipos principais:
# feature/   - Nova funcionalidade
# fix/       - Correção de bug
# refactor/  - Melhorias de código
# docs/      - Melhorias de documentação
```

### 3. Fazer Commits

```bash
# Commits devem ser pequenos e focados
git commit -m "feat: adicionar validação de CPF no login"

# Formato: tipo: descrição
# Tipos: feat, fix, refactor, docs, test, style, perf
```

### 4. Push e PR

```bash
# Push para seu fork
git push origin feature/nome-descritivo

# Abra PR no GitHub
# Preencha template completamente
```

### 5. Code Review

Responda comentários e faça ajustes necessários.

### 6. Merge

Após aprovação, seu PR será mergeado!

---

## 💬 Mensagens de Commit

Seguimos convenção padrão: `tipo: descrição`

### Tipos Disponíveis

| Tipo | Descrição | Exemplo |
|------|-----------|---------|
| `feat` | Nova feature | `feat: adicionar 2FA` |
| `fix` | Correção de bug | `fix: corrigir login CPF` |
| `refactor` | Refatoração | `refactor: simplificar service` |
| `docs` | Documentação | `docs: atualizar README` |
| `test` | Testes | `test: adicionar testes login` |
| `style` | Formatação | `style: ajustar indentação` |
| `perf` | Performance | `perf: otimizar query banco` |
| `chore` | Build/deps | `chore: atualizar Spring Boot` |

### Exemplos

```bash
# ✅ Bom
git commit -m "feat: adicionar filtro por data em registros"
git commit -m "fix: resolver erro ao deletar funcionário"
git commit -m "refactor: extrair lógica de validação para classe"
git commit -m "test: adicionar testes para JwtTokenProvider"

# ❌ Ruim
git commit -m "Melhorias"
git commit -m "Fix bug"
git commit -m "Updated stuff"
```

---

## 📝 Padrões de Código

### Java/Spring Boot

#### Nomeação
```java
// ✅ Bom
private String employeeName;
public void createTimeRecord() {}
class EmployeeService {}

// ❌ Ruim
private String name;
public void create() {}
class Service {}
```

#### Anotações
```java
// Use Lombok para reduzir boilerplate
@Data           // getter, setter, toString
@Builder        // padrão builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    // ...
}
```

#### Logging
```java
// Use SLF4J com Slf4j
@Slf4j
public class EmployeeService {
    public void register(Employee employee) {
        log.info("Registering employee: {}", employee.getCpf());
        // ...
    }
}
```

#### Exceções
```java
// Crie exceções customizadas quando apropriado
public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(String cpf) {
        super("Employee not found with CPF: " + cpf);
    }
}
```

#### Validação
```java
// Use anotações de validação
public class EmployeeRegistrationRequest {
    @NotNull
    @Size(min = 11, max = 11, message = "CPF must have 11 digits")
    private String cpf;
    
    @NotNull
    @Size(min = 6, message = "Password must have at least 6 characters")
    private String password;
}
```

### JavaScript

#### Nomeação
```javascript
// ✅ Bom
const API_BASE_URL = 'http://localhost:8080/api';
function getEmployeeTimeRecords() {}
const currentUser = {};

// ❌ Ruim
const api_url = 'http://localhost:8080/api';
function get() {}
const usr = {};
```

#### Async/Await
```javascript
// ✅ Bom
async function loadRecords() {
    try {
        const records = await apiRequest('/employee/time-records');
        displayRecords(records);
    } catch (error) {
        showError('Erro ao carregar registros');
    }
}

// ❌ Ruim
function loadRecords() {
    apiRequest('/employee/time-records').then(records => {
        displayRecords(records);
    }).catch(e => console.log(e));
}
```

#### Comentários
```javascript
// ✅ Bom
// Carrega registros de ponto do funcionário
async function loadRecords() {}

// ❌ Ruim
// função (óbvio)
function loadRecords() {}

// TODO: Implementar paginação infinita
// XXX: Remover após corrigir bug #123
```

### CSS

#### Variáveis
```css
/* ✅ Use variáveis CSS */
:root {
    --primary: #2563eb;
    --secondary: #10b981;
}

.button-primary {
    background-color: var(--primary);
}

/* ❌ Avoid */
.button-primary {
    background-color: #2563eb;
}
```

#### BEM Naming
```css
/* ✅ Bom */
.employee-card {}
.employee-card__header {}
.employee-card__actions {}
.employee-card--active {}

/* ❌ Ruim */
.card {}
.card-header {}
.emp-card {}
```

---

## ✅ Testes

Adicione testes para novas features!

### Java - JUnit 5

```java
@DisplayName("AuthenticationService")
class AuthenticationServiceImplTest {
    
    @Test
    @DisplayName("Should authenticate employee successfully")
    void testAuthenticateSuccessfully() {
        // Arrange
        LoginRequest request = new LoginRequest("12345678900", "password123");
        Employee employee = new Employee();
        
        // Act
        JwtAuthenticationResponse response = authService.authenticate(request);
        
        // Assert
        assertNotNull(response.getToken());
        assertEquals("12345678900", response.getCpf());
    }
}
```

### JavaScript

```javascript
// ✅ Use descrições claras
describe('API Client', () => {
    it('should successfully login with valid credentials', async () => {
        const result = await login('12345678900', 'password123');
        expect(result.token).toBeDefined();
    });
    
    it('should throw error with invalid credentials', async () => {
        await expect(login('invalid', 'password')).rejects.toThrow();
    });
});
```

---

## 🔍 Pull Request

### Template

```markdown
## Descrição
Descreva o que esta PR faz.

## Tipo de Mudança
- [ ] Bug fix
- [ ] Nova feature
- [ ] Mudança em documentação
- [ ] Refatoração

## Mudanças
- Item 1
- Item 2

## Testado?
- [ ] Sim
- [ ] Não

## Screenshots (se aplicável)
[Cole aqui]

## Checklist
- [ ] Código segue padrões do projeto
- [ ] Testes adicionados/passando
- [ ] Documentação atualizada
- [ ] Sem breaking changes
```

### Boas Práticas

1. **Uma coisa por PR**
   - Não misture feature com fix
   - Mantenha PRs focadas

2. **Commits limpos**
   - Rebase antes de enviar
   - Squash commits desnecessários

3. **Mensagem descritiva**
   - Título em até 72 caracteres
   - Corpo com detalhes

4. **Teste localmente**
   - `mvn test` para Java
   - Teste manual no navegador

5. **Sem conflitos**
   - Rebase com `upstream/main`

---

## 💬 Comunicação

### Canais

- **Issues** - Bugs e features
- **Discussions** - Perguntas e ideias
- **Pull Requests** - Código review

### Etiquetas (Labels)

| Label | Significado |
|-------|------------|
| `bug` | Relatório de bug |
| `feature` | Solicitação de feature |
| `documentation` | Melhoria de docs |
| `good first issue` | Para novos contribuidores |
| `help wanted` | Precisa de ajuda |
| `blocked` | Bloqueada por outro PR |

---

## 🎓 Aprendizado

### Recursos para Aprender

#### Spring Boot & Java
- [Spring Official Tutorial](https://spring.io/guides)
- [Baeldung Spring Boot](https://www.baeldung.com/spring-boot)
- [Effective Java](https://www.oreilly.com/library/view/effective-java-3rd/9780134686639/)

#### JavaScript
- [MDN Web Docs](https://developer.mozilla.org/)
- [JavaScript.info](https://javascript.info/)
- [JS Best Practices](https://google.github.io/styleguide/javascriptguide.xml)

#### Testing
- [JUnit 5 Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Testing Best Practices](https://testingjavascript.com/)

---

## 🏆 Reconhecimento

Contribuidores destacados serão reconhecidos em:
- Arquivo [CONTRIBUTORS.md](CONTRIBUTORS.md) (em breve)
- README.md
- Releases

---

## ❓ Dúvidas?

1. Verifique [Issues](https://github.com/seu-usuario/time-tracking/issues)
2. Abra uma [Discussion](https://github.com/seu-usuario/time-tracking/discussions)
3. Leia documentação em detalhes

---

**Obrigado por contribuir! 🎉**

Suas contribuições fazem este projeto melhor.

