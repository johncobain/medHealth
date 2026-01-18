# MedHealth - Sistema de Gerenciamento de Clínica Médica

Este é o repositório para o sistema MedHealth, uma aplicação para gerenciamento de consultas em uma clínica médica. O projeto é dividido em uma arquitetura de microsserviços para garantir escalabilidade e manutenibilidade.

## Visão Geral da Arquitetura

O sistema é composto por três componentes principais:

1. **API Principal (`medHealthAPI`)**: O backend principal construído com Spring Boot, responsável por gerenciar a lógica de negócio principal, como cadastro de médicos, pacientes e agendamento de consultas.
2. **Microsserviço de Notificação (`medHealthMS`)**: Um serviço Spring Boot dedicado a enviar notificações sobre agendamentos, cancelamentos, etc.
3. **Frontend (`medHealthFE`)**: A interface do usuário construída com React e Vite, que consome os serviços do backend.

## Tecnologias Utilizadas

- **Backend (API & Microsserviço)**:
  - Java 21
  - Spring Boot
  - Spring Data JPA / Hibernate
  - Spring Security (com JWT)
  - PostgreSQL
  - Maven
  - Flyway (para migrações de banco de dados)
- **Frontend**:
  - React
  - Vite
  - Node.js / npm
- **Containerização**:
  - Docker & Docker Compose

## Pré-requisitos

Para executar o projeto localmente, você precisará ter instalado:

- Java (JDK 21 ou superior)
- Maven
- Node.js e npm
- Docker e Docker Compose

## Como Executar o Projeto

O projeto inclui um script de gerenciamento (`app.sh`) para facilitar a execução e o gerenciamento dos serviços.

### 1. Usando Docker (Recomendado)

Este método irá construir as imagens e iniciar todos os contêineres (Banco de Dados, API, Microsserviço e Frontend).

// TODO: Adicionar instruções de Docker quando o Dockerfile e docker-compose.yml forem criados.

### 2. Executando Localmente (Para Desenvolvimento)

Você pode executar cada serviço individualmente pela IDE ou diretamente pelo terminal.

#### 2.1. Configurando o Banco de Dados

```bash
docker compose up -d db
```

#### 2.2. Executando a API e o Microsserviço

```bash
cd medHealthAPI
mvn clean install
mvn spring-boot:run
```

Em outro terminal, para o microsserviço:

```bash
cd medHealthMS
mvn clean install
mvn spring-boot:run
```

#### 2.3. Executando o Frontend

```bash
cd medHealthFE
npm install
npm run dev
```

### 3. Usando o Script `app.sh`

O script `app.sh` oferece uma maneira simples e eficiente de executar o projeto localmente.

**Passo 1: Iniciar o Banco de Dados**
O banco de dados precisa estar em execução antes de iniciar a API ou o microsserviço.

```bash
./app.sh start-db
```

**Passo 2: Iniciar os Serviços**
Abra um terminal para cada serviço que deseja executar.

```bash
# Para executar a API principal
./app.sh run api

# Para executar o microsserviço de notificação
./app.sh run ms

# Para executar o frontend em modo de desenvolvimento
./app.sh run front
```

**Passo 3: Parar o Banco de Dados**
Quando terminar, você pode parar o contêiner do banco de dados.

```bash
./app.sh stop-db
```

## Comandos Úteis do `app.sh`

O script `app.sh` oferece vários comandos para ajudar no desenvolvimento:

Para ver todos os comandos, execute `./app.sh`.
