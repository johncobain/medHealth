# medHealthNotifications

Microsserviço de notificações: consome mensagens da fila RabbitMQ e envia e-mails (confirmação de agendamento, recuperação de senha, etc.). Usa PostgreSQL próprio (`db-mail`) para registro de envios.

**Stack:** Spring Boot, Spring AMQP (RabbitMQ), Spring Data JPA, PostgreSQL.

- Rodar local: `./app.sh run ms` (exige `./app.sh db`, `./app.sh rabbit` antes).
- Configurar variáveis de e-mail em `.env` (copiar de `.env.example`).
- Ver [README principal](../README.md) para formas de execução e estrutura do projeto.
