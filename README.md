# 🏦 Bank API

API REST de banco digital desenvolvida em Java com Spring Boot 3.5.

---

## ✨ Funcionalidades

- **Autenticação** — registro e login com JWT
- **Contas bancárias** — criação automática ao registrar, consulta de saldo e extrato
- **Depósitos e saques** — movimentação financeira
- **Transferências** — entre contas cadastradas
- **Histórico de transações** — extrato completo

---

## 🛠️ Tecnologias

| Tecnologia | Uso |
|---|---|
| [Java 21](https://openjdk.org/) | Linguagem principal |
| [Spring Boot 3.5](https://spring.io/projects/spring-boot) | Framework web |
| [Spring Security](https://spring.io/projects/spring-security) | Autenticação e autorização |
| [Spring Data JPA](https://spring.io/projects/spring-data-jpa) | ORM para banco de dados |
| [PostgreSQL](https://www.postgresql.org/) | Banco de dados |
| [Neon](https://neon.tech/) | Hospedagem do banco |
| [JWT](https://jwt.io/) | Tokens de autenticação |
| [Lombok](https://projectlombok.org/) | Redução de boilerplate |
| [Maven](https://maven.apache.org/) | Gerenciamento de dependências |

---

## 🚀 Como rodar localmente

### Pré-requisitos

- [Java 21](https://adoptium.net/temurin/releases/?version=21)
- [Maven](https://maven.apache.org/download.cgi)
- Banco PostgreSQL (ou conta no [Neon](https://neon.tech))

### Instalação

```bash
git clone https://github.com/cauamconceicao/bank-api.git
cd bank-api
```

Configure o `src/main/resources/application.properties` com suas credenciais:

```properties
spring.datasource.url=jdbc:postgresql://SEU_HOST/SEU_BANCO?sslmode=require
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
```

```bash
mvn spring-boot:run
```

A API estará disponível em `http://localhost:8080`

---

## 📋 Endpoints

### Auth
| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/auth/register` | Registrar novo usuário |
| POST | `/api/auth/login` | Fazer login e obter token JWT |

### Conta
| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/account` | Ver dados e saldo da conta |
| GET | `/api/account/statement` | Extrato de transações |

### Transações
| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/transactions/deposit` | Realizar depósito |
| POST | `/api/transactions/withdraw` | Realizar saque |
| POST | `/api/transactions/transfer` | Transferir para outra conta |

---

## 🔐 Autenticação

Todos os endpoints (exceto registro e login) requerem autenticação via JWT.

Após o login, inclua o token no header:

```
Authorization: Bearer {seu_token}
```

---

## 📝 Exemplos de uso

### Registro

```json
POST /api/auth/register
{
  "name": "Cauã",
  "email": "cauamartins2005@gmail.com",
  "password": "123456",
  "cpf": "12345678900"
}
```

### Login

```json
POST /api/auth/login
{
  "email": "cauamartins2005@gmail.com",
  "password": "123456"
}
```

### Depósito

```json
POST /api/transactions/deposit
Authorization: Bearer {token}

{
  "amount": 1000.00,
  "description": "Depósito inicial"
}
```

### Transferência

```json
POST /api/transactions/transfer
Authorization: Bearer {token}

{
  "targetAccountNumber": "NUMERO_DA_CONTA_DESTINO",
  "amount": 200.00,
  "description": "Pagamento"
}
```

---

## 📁 Estrutura do projeto

```
bank-api/
├── src/main/java/com/bankapi/
│   ├── controller/       # Endpoints da API
│   ├── service/          # Lógica de negócio
│   ├── repository/       # Acesso ao banco
│   ├── model/            # Entidades JPA
│   ├── dto/              # Objetos de transferência
│   ├── security/         # JWT e Spring Security
│   └── exception/        # Tratamento de erros
└── src/main/resources/
    └── application.properties
```

---

## 📄 Licença

MIT © [Cauã Conceição](https://github.com/cauamconceicao)