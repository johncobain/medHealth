# MedHealth

Sistema de gestão de clínica médica: cadastro de médicos e pacientes, agendamento de consultas, autenticação e notificações por e-mail. Arquitetura em microsserviços com Spring Boot e React.

## Colaboradores

- [Andrey Gomes](https://github.com/johncobain)
- [Gabriel Nascimento](https://github.com/uKuroo)
- [Lara Carolina](https://github.com/laraeucarolina)

## Vídeo de Demonstração do Projeto

Clique na imagem para assistir ao vídeo:
[![Vídeo de Demonstração](thumbnail.png)](https://youtu.be/1iiOwpajZKM?si=rQ0u2mIF_MOhs9OC)

## Estrutura do projeto

```plaintext
medHealth/
├── medHealthAPI/             # API principal (médicos, pacientes, consultas, auth)
├── medHealthEureka/          # Servidor de descoberta (Eureka)
├── medHealthGateway/         # API Gateway (roteamento, porta 8080)
├── medHealthNotifications/   # Serviço de e-mail (consumidor RabbitMQ)
├── medHealthFE/              # Frontend React (Vite)
├── docker-compose.yml        # Orquestração dos serviços
└── app.sh                    # Script para rodar/gerenciar o projeto
```

| Subprojeto                                                 | Descrição                                                                                                                    |
| ---------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------- |
| [medHealthAPI](medHealthAPI/README.md)                     | Backend principal: usuários, médicos, pacientes, agendamentos, autenticação JWT. Expõe REST e publica mensagens no RabbitMQ. |
| [medHealthEureka](medHealthEureka/README.md)               | Servidor Eureka para registro e descoberta dos microsserviços.                                                               |
| [medHealthGateway](medHealthGateway/README.md)             | Gateway que roteia as requisições do frontend para a API via Eureka.                                                         |
| [medHealthNotifications](medHealthNotifications/README.md) | Microsserviço que consome filas RabbitMQ e envia e-mails.                                                                    |
| [medHealthFE](medHealthFE/README.md)                       | Interface em React: login, dashboard, CRUD de médicos/pacientes, consultas e configurações.                                  |

## Pré-requisitos

- **Para rodar local (só infra no Docker):** Java 21, Maven, Node.js, Docker
- **Para rodar tudo no Docker:** Docker e Docker Compose

## Formas de executar

### 1. Desenvolvimento local (infra no Docker, apps locais)

Sobe apenas banco principal, banco de e-mail e RabbitMQ no Docker; API, Eureka, Gateway, Notifications e frontend rodam na sua máquina.

```bash
./app.sh db        # sobe db + db-mail
./app.sh rabbit    # sobe RabbitMQ
./app.sh run eureka     # terminal 1
./app.sh run gateway    # terminal 2
./app.sh run api        # terminal 3
./app.sh run ms         # terminal 4 (notifications)
./app.sh run frontend   # terminal 5
```

- Frontend: <http://localhost:5173>
- API (direto): <http://localhost:8081>
- Gateway: <http://localhost:8080>
- Eureka: <http://localhost:8761>
- RabbitMQ: <http://localhost:15672> (rabbituser / rabbitpass)

O frontend pode apontar o Gateway (`VITE_API_BASE_URL=http://localhost:8080`) conforme configuração no `.env` do frontend.

### 2. Tudo no Docker

Sobe todos os serviços definidos no `docker-compose` (bancos, RabbitMQ, Eureka, Gateway, API, Notifications, frontend).

```bash
./app.sh up
```

- Frontend: <http://localhost:5173>
- Gateway (base da API): <http://localhost:8080/medHealth>
- Eureka: <http://localhost:8761>
- RabbitMQ: <http://localhost:15672>

Para parar:

```bash
./app.sh down
```

### 3. Apenas backend no Docker

Útil para desenvolver o frontend local apontando para o backend em containers.

```bash
./app.sh backend   # db, db-mail, rabbitmq, eureka, api, notifications, gateway
./app.sh run frontend
```

### 4. Outros comandos úteis do `app.sh`

- `./app.sh` — lista todos os comandos e fluxo recomendado
- `./app.sh db-stop` / `./app.sh rabbit-stop` — para bancos e RabbitMQ
- `./app.sh backend-stop` — para API, Notifications, Eureka e Gateway
- `./app.sh frontend` — sobe só o frontend no Docker
- `./app.sh status` ou `./app.sh ps` — status dos containers
- `./app.sh logs [serviço]` — logs (ex.: `./app.sh logs api`)

### 5. Sem o script `app.sh`

Caso não seja possível usar o script `app.sh` (Windows sem WSL/Git Bash), use os comandos abaixo. No PowerShell ou CMD, utilizando `docker compose` ou `docker-compose`, dependendo da versão do Docker.

**Desenvolvimento local (infra no Docker, apps na máquina):**

```bash
# 1. Subir bancos e RabbitMQ
docker compose up -d db db-mail rabbitmq

# 2. Em terminais separados, na raiz do projeto:
cd medHealthEureka    && mvn spring-boot:run
cd medHealthGateway   && mvn spring-boot:run
cd medHealthAPI       && mvn spring-boot:run
cd medHealthNotifications && mvn spring-boot:run
cd medHealthFE        && npm install && npm run dev
```

**Tudo no Docker:**

```bash
docker compose up -d
# Para parar:
docker compose down
```

**Apenas backend no Docker, frontend local:**

```bash
docker compose up -d db db-mail rabbitmq eureka
# Aguardar Eureka ficar saudável, depois:
docker compose up -d api notifications gateway
# Em outro terminal:
cd medHealthFE && npm run dev
```

**Comandos úteis sem o script:**

| Ação               | Comando                          |
| ------------------ | -------------------------------- |
| Parar bancos       | `docker compose stop db db-mail` |
| Parar RabbitMQ     | `docker compose stop rabbitmq`   |
| Status             | `docker compose ps`              |
| Logs de um serviço | `docker compose logs -f api`     |

## Tecnologias

- **Backend:** Java 21, Spring Boot, Spring Security (JWT), Spring Data JPA, Flyway, RabbitMQ
- **Frontend:** React, Vite
- **Infra:** PostgreSQL (2 instâncias: app + mail), RabbitMQ, Docker

---

Documentação de cada módulo: ver links na tabela da [Estrutura do projeto](#estrutura-do-projeto).
