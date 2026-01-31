# medHealthEureka

Servidor de descoberta (Eureka): registra API, Gateway e Notifications para que o Gateway possa rotear as requisições. Deve ser iniciado antes do Gateway e dos demais microsserviços.

**Stack:** Spring Boot, Netflix Eureka Server.

- Rodar local: `./app.sh run eureka` (não depende de banco).
- Dashboard: http://localhost:8761
- Ver [README principal](../README.md) para formas de execução e estrutura do projeto.
