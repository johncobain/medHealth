# medHealthGateway

API Gateway do MedHealth: recebe as requisições do frontend e roteia para a API e outros serviços registrados no Eureka (porta 8080). Único ponto de entrada quando se usa a stack completa em Docker.

**Stack:** Spring Boot, Spring Cloud Gateway, Eureka Client.

- Rodar local: `./app.sh run gateway` (exige Eureka em execução).
- Ver [README principal](../README.md) para formas de execução e estrutura do projeto.
