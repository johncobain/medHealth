# medHealthAPI

API principal do MedHealth: cadastro de médicos e pacientes, agendamento de consultas, autenticação (JWT), solicitações de cadastro de médico e envio de mensagens para o serviço de notificações via RabbitMQ.

**Stack:** Spring Boot, Spring Security, Spring Data JPA, Flyway, PostgreSQL, RabbitMQ.

- Rodar local: `./app.sh run api` (exige `./app.sh db` e `./app.sh rabbit` antes).
- Ver [README principal](../README.md) para formas de execução e estrutura do projeto.
